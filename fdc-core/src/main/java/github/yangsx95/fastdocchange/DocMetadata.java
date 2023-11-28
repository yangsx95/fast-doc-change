package github.yangsx95.fastdocchange;

/**
 * 单据信息元数据，用于存储一些额外的业务信息
 * 元数据一经创建，不可修改，只可追加
 *
 * @author yangshunxiang
 * @since 2023/11/27
 */
public interface DocMetadata {

    /**
     * 增加单据信息元数据
     *
     * @return 单据信息元数据是否增加成功，true：增加成功，false：增加失败
     */
    boolean add(String key, Object value);

    /**
     * 单据信息元数据是否包含指定key
     *
     * @return true：包含，false：不包含
     */
    boolean containsKey(String key);

    /**
     * 获取单据信息元数据，如果不存在则返回null
     *
     * @return 单据信息元数据
     */
    Object get(String key);
}
