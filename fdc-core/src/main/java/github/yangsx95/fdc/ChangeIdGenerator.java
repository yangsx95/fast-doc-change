package github.yangsx95.fdc;

/**
 * changeNo生成器
 *
 * @author yangshunxiang
 * @since 2023/11/28
 */
public interface ChangeIdGenerator {

    /**
     * 生成changeNo
     *
     * @param changeRecord 变更记录
     * @param version      版本次序
     * @return changeNo
     */
    String generate(ChangeRecord changeRecord, Integer version);

}
