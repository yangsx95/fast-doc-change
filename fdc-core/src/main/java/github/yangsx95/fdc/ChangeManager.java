package github.yangsx95.fdc;

/**
 * 变更的生命周期管理器，主要对变更状态进行管理
 *
 * @author yangshunxiang
 * @see ChangeState
 * @see ChangeModel
 * @since 2023/11/27
 */
public interface ChangeManager {

    /**
     * 暂存变更，暂存后的变更会变为已暂存的状态
     *
     * @param changeRecord 变更记录
     */
    ChangeModel stash(ChangeRecord changeRecord);

    /**
     * 提交变更，提交后的变更会变为已提交的状态
     *
     * @param changeId 变更id
     */
    void commit(String changeId);

    /**
     * 发布变更，发布后的变更会变为已发布的状态
     *
     * @param changeId 变更id
     */
    void publish(String changeId);

    /**
     * 作废变更，作废后的变更会变为已作废的状态
     *
     * @param changeId 变更id
     */
    void voidChange(String changeId);

    /**
     * 取消变更，取消后的变更会变为已取消的状态
     *
     * @param changeId 变更id
     */
    void cancel(String changeId);

    /**
     * 获取变更编号对应的变更
     *
     * @param changeId 变更id
     * @return 变更，如果没有变更则返回null
     */
    ChangeModel getChangeById(String changeId);

    /**
     * 获取最新的版本次序的变更，不会返回处于已取消状态的变更
     *
     * @param docNo   单据编号
     * @param docType 单据类型
     * @return 最新的版本次序的变更，如果没有变更则返回null
     */
    ChangeModel getNewestChange(String docNo, String docType);

}
