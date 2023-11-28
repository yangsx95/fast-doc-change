package github.yangsx95.fastdocchange;

import java.util.Objects;

/**
 * 变更管理器抽象
 * 主要提供了变更状态的管理的实现
 *
 * @author yangshunxiang
 * @since 2023/11/27
 */
public abstract class AbstractChangeManager implements ChangeManager {

    private final ChangeManagerConfig changeManagerConfig;

    public AbstractChangeManager(ChangeManagerConfig changeManagerConfig) {
        if (Objects.isNull(changeManagerConfig)) {
            throw new IllegalArgumentException("changeManagerConfig can not be null");
        }
        this.changeManagerConfig = changeManagerConfig;
    }

    @Override
    public ChangeModel stash(ChangeRecord changeRecord) {
        if (Objects.isNull(changeRecord) || !changeRecord.isValid()) {
            throw new IllegalArgumentException("changeRecord is invalid");
        }

        // 如果是初始版本，或者上个版本的生命周期结束，则重新生成一个变更
        ChangeModel newestChange = getNewestChange(changeRecord.docNo(), changeRecord.docType());
        if (Objects.isNull(newestChange) || newestChange.getChangeState().isEndState()) {
            ChangeModel changeModel = new ChangeModel();
            changeModel.setChangeRecord(changeRecord);
            changeModel.setChangeState(ChangeState.STASHED);
            changeModel.setVersion(getNextVersion(changeRecord.docNo(), changeRecord.docType()));
            changeModel.setChangeId(changeManagerConfig.changeIdGenerator().generate(changeRecord, changeModel.getVersion()));
            // 持久化变更
            persistChangeModel(changeModel);
            return changeModel;
        }

        // 如果非初始版本，且最新版本生命周期未结束，使用状态机判断是否可进行暂存操作
        ChangeState nextState = ChangeStateMachine.fastFireAndGetState(newestChange.getChangeState(), ChangeEvent.STASH, changeManagerConfig.eventCanNotFireHandler());
        // 如果可以暂存，替换内容，并重新暂存
        newestChange.setChangeState(nextState);
        newestChange.setChangeRecord(changeRecord);
        persistChangeModel(newestChange);
        return newestChange;
    }

    @Override
    public void commit(String changeId) {
        doSendChangeEventByChangeId(changeId, ChangeEvent.COMMIT);
    }

    @Override
    public void publish(String changeId) {
        doSendChangeEventByChangeId(changeId, ChangeEvent.PUBLISH);
    }

    @Override
    public void voidChange(String changeId) {
        doSendChangeEventByChangeId(changeId, ChangeEvent.VOID);
    }

    @Override
    public void cancel(String changeId) {
        doSendChangeEventByChangeId(changeId, ChangeEvent.CANCEL);
    }

    private void doSendChangeEventByChangeId(String changeId, ChangeEvent commit) {
        if (Objects.isNull(changeId) || changeId.isBlank()) {
            throw new IllegalArgumentException("changeId can not be null or blank");
        }

        ChangeModel changeModel = getChangeById(changeId);
        if (Objects.isNull(changeModel)) {
            throw new IllegalArgumentException("can not find changeModel by changeId: " + changeId);
        }

        ChangeState nextState = ChangeStateMachine.fastFireAndGetState(changeModel.getChangeState(), commit, changeManagerConfig.eventCanNotFireHandler());
        changeModel.setChangeState(nextState);
        persistChangeState(changeModel.getChangeId(), nextState);
    }

    /**
     * 持久化变更
     *
     * @param changeModel 变更
     */
    protected abstract void persistChangeModel(ChangeModel changeModel);

    /**
     * 持久化变更状态
     *
     * @param changeId    变更id
     * @param changeState 变更状态
     */
    protected abstract void persistChangeState(String changeId, ChangeState changeState);

    /**
     * 获取最新的版本次序
     *
     * @param docNo   单据号
     * @param docType 单据类型
     * @return 最新的版本次序，如果没有任何版本，则返回1
     */
    protected abstract Integer getNextVersion(String docNo, String docType);
}
