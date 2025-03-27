package interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts({
    @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class}
    )
})
public class MyBatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // StatementHandler 얻기
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // BoundSql 얻기 (실제 SQL 쿼리를 담고 있음)
        BoundSql boundSql = statementHandler.getBoundSql();

        // parameterObject 가져오기
        Object parameterObject = boundSql.getParameterObject();

        // 파라미터 객체가 Map이라면
        if (parameterObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> paramMap = (Map<String, Object>) parameterObject;

            // pageNumber와 pageSize 가져오기
            Long offset = (Long) paramMap.get("offset");
            Long limit = (Long) paramMap.get("limit");

            if (offset != null && limit != null) {
                // offset과 limit 계산

                // 쿼리 수정 (LIMIT과 OFFSET 추가)
                String originalSql = boundSql.getSql();
                System.out.println("originalSql : " + originalSql);
                String modifiedSql = originalSql + " LIMIT " + offset + ", " + limit.intValue();

                // 수정된 SQL 쿼리 설정
                Field sqlField = BoundSql.class.getDeclaredField("sql");
                sqlField.setAccessible(true);
                sqlField.set(boundSql, modifiedSql);
            }
        }

        // 쿼리 실행
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}

