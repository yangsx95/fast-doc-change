package github.yangsx95.fastdocchange;

/**
 * 变更记录对象，变更记录一旦创建就不可修改
 *
 * @author yangshunxiang
 * @since 2023/11/27
 */
public record ChangeRecord(
        String docNo,
        String docType,
        String content,
        // 变更记录的元数据，可以用来存储一些额外的单据信息
        DocMetadata metadata
) {

    /**
     * 判断变更记录是否合法
     */
    public boolean isValid() {
        return docNo != null
                && !docNo.isBlank()
                && docType != null
                && !docType.isBlank()
                && content != null
                ;
    }
}
