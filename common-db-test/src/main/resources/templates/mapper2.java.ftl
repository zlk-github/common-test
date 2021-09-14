package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

<#--/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */-->
/**
* @author  likuan.zhou
* @title:  ${table.mapperName}
* @description: TODO
* @date ${date}
*/
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
