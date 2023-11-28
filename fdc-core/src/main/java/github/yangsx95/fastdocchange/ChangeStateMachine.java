package github.yangsx95.fastdocchange;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author yangshunxiang
 * @since 2023/11/27
 */
public class ChangeStateMachine {

    /**
     * 状态机配置
     */
    private static final StateMachineConfig<ChangeState, ChangeEvent> CONFIG = new StateMachineConfig<>();
    public static final BiFunction<ChangeState, ChangeEvent, RuntimeException> CHANGE_STATE_CHANGE_EVENT_BI_CONSUMER =
            (state, event) -> new IllegalStateException("当前状态为：" + state + "，不允许执行事件：" + event);

    private final StateMachine<ChangeState, ChangeEvent> stateMachine;

    static {
        CONFIG.configure(ChangeState.STASHED)
                // ignore 代表经过该事件后，状态不变
                .ignore(ChangeEvent.STASH)
                // permit的中文含义为允许，即允许从前置态-接受到事件->后置态
                .permit(ChangeEvent.CANCEL, ChangeState.CANCELLED)
                .permit(ChangeEvent.COMMIT, ChangeState.COMMITTED)
        ;

        CONFIG.configure(ChangeState.COMMITTED)
                .permit(ChangeEvent.PUBLISH, ChangeState.PUBLISHED)
                .permit(ChangeEvent.VOID, ChangeState.VOIDED)
        ;
    }

    private final BiFunction<ChangeState, ChangeEvent, RuntimeException> eventCanNotFireHandler;

    public ChangeStateMachine(ChangeState initState) {
        this(initState, CHANGE_STATE_CHANGE_EVENT_BI_CONSUMER);
    }

    public ChangeStateMachine(ChangeState initState, BiFunction<ChangeState, ChangeEvent, RuntimeException> eventCanNotFireHandler) {
        if (Objects.isNull(initState)) {
            throw new IllegalArgumentException("初始状态不能为空");
        }
        if (Objects.isNull(eventCanNotFireHandler)) {
            throw new IllegalArgumentException("事件不允许执行时的处理器不能为空");
        }

        stateMachine = new StateMachine<>(initState, CONFIG);
        this.eventCanNotFireHandler = eventCanNotFireHandler;
    }

    public ChangeState getState() {
        return stateMachine.getState();
    }

    public boolean canFire(ChangeEvent event) {
        return stateMachine.canFire(event);
    }

    public void fire(ChangeEvent event) {
        if (stateMachine.canFire(event)) {
            stateMachine.fire(event);
        } else {
            throw eventCanNotFireHandler.apply(stateMachine.getState(), event);
        }
    }

    /**
     * 快速执行事件并返回最终状态
     *
     * @param state 状态
     * @param event 事件
     * @return 最终状态
     */
    public static ChangeState fastFireAndGetState(ChangeState state,
                                                  ChangeEvent event,
                                                  BiFunction<ChangeState, ChangeEvent, RuntimeException> eventCanNotFireHandler) {
        ChangeStateMachine stateMachine = new ChangeStateMachine(state);
        if (stateMachine.canFire(event)) {
            stateMachine.fire(event);
            return stateMachine.getState();
        }
        if (Objects.isNull(eventCanNotFireHandler)) {
            eventCanNotFireHandler = CHANGE_STATE_CHANGE_EVENT_BI_CONSUMER;
        }
        throw eventCanNotFireHandler.apply(stateMachine.getState(), event);
    }
}