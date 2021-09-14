package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

<#--/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */-->
/**
* @author  likuan.zhou
* @title:  ${table.serviceName}
* @description: TODO
* @date ${date}
*/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>
