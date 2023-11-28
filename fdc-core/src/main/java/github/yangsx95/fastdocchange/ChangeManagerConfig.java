package github.yangsx95.fastdocchange;

import java.util.function.BiFunction;

/**
 * @author yangshunxiang
 * @since 2023/11/28
 */
public record ChangeManagerConfig(
        ChangeIdGenerator changeIdGenerator,
        BiFunction<ChangeState, ChangeEvent, RuntimeException> eventCanNotFireHandler
) {

}
