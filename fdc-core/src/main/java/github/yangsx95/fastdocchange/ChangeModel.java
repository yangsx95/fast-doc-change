package github.yangsx95.fastdocchange;

import lombok.Data;

/**
 * 变更记录模型
 *
 * @author yangshunxiang
 * @since 2023/11/27
 */
@Data
public class ChangeModel {

    /**
     * 变更id，无论何种单据类型，全局唯一，由changeNoGenerator生成
     */
    private String changeId;

    /**
     * 变更的版本次序，依次递增，从1开始
     */
    private Integer version;

    /**
     * 变更记录，也是变更的具体单据内容 全量
     */
    private ChangeRecord changeRecord;

    /**
     * 变更状态
     */
    private ChangeState changeState;

}
