package github.yangsx95.fdc;

/**
 * 变更状态枚举
 *
 * @author yangshunxiang
 * @since 2023/11/27
 */
public enum ChangeState {

    /**
     * 已暂存
     */
    STASHED,
    /**
     * 已提交
     */
    COMMITTED,
    /**
     * 已发布
     */
    PUBLISHED,
    /**
     * 已作废
     */
    VOIDED,
    /**
     * 已取消
     */
    CANCELLED,
    ;

    /**
     * 生命周期结束状态判断
     */
    public boolean isEndState() {
        return this == VOIDED || this == CANCELLED || this == PUBLISHED;
    }
}
