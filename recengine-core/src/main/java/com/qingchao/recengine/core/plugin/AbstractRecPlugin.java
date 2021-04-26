package com.qingchao.recengine.core.plugin;

import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 3:27 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractRecPlugin<PC extends AbstractRecPluginContext> {
    public static final String NAMESPACE = "namespace";
    public static final String TABLE_NAME = "tableName";
    public static final String PRIMARY_KEY_COLUMN = "primaryKeyColumn";
    public static final String COLUMN_NAME = "columnName";
    public static final String NAME = "name";


    protected PC pluginContext;

}
