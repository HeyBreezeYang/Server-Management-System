package com.system.started.db;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dbService")
public class DBService
{
	@Autowired
    private SqlSessionTemplate ossSqlSessionTemplate;

    public HashMap<String, Object> selectByPage( String statement, HashMap<String, Object> parameter, int currentPage, int perPage )
    {
        // 查询总数
        List<HashMap<String, Object>> resultCountList = ossSqlSessionTemplate.selectList( statement + "Count", parameter );
        long totalSize = (long) (resultCountList.get( 0 ).get( "totalSize" ));

        // 计算当前其实序号，mysql从0开始计算
        int beginIndex = (currentPage - 1) * perPage;
        parameter.put( "beginIndex", beginIndex );
        parameter.put( "perPage", perPage );
        
        // 查询分页数据
        List<HashMap<String, Object>> resultList = ossSqlSessionTemplate.selectList( statement, parameter );

        HashMap<String, Object> resultMap = new HashMap<>();
        long tempPage = totalSize / perPage;
        long tempSize = totalSize % perPage;
        long totalPage = tempPage == 0 ? 1 : tempSize == 0 ? tempPage : tempPage + 1;
        resultMap.put( "recordsTotal", totalSize );
        resultMap.put( "recordsFiltered", totalSize );
        resultMap.put( "totalPage", totalPage );
        resultMap.put( "currentPage", currentPage );
        resultMap.put( "perPage", perPage );
        resultMap.put( "record", resultList );

        return resultMap;
    }

    public HashMap<String, Object> select( String statement, HashMap<String, Object> parameter )
    {
        HashMap<String, Object> resultMap = new HashMap<>();
        // 查询数据
        List<HashMap<String, Object>> resultList = ossSqlSessionTemplate.selectList( statement, parameter );
        resultMap.put( "record", resultList );
        resultMap.put( "recordsTotal", resultList.size() );
        resultMap.put( "recordsFiltered", resultList.size() );
        resultMap.put( "totalPage", 1 );
        resultMap.put( "currentPage", 1 );
        resultMap.put( "perPage", resultList.size() );
        return resultMap;
    }
    
    public List<HashMap<String, Object>> directSelect( String statement, HashMap<String, Object> parameter )
    {
        return ossSqlSessionTemplate.selectList( statement, parameter );
    }

    public Object selectOne( String statement, HashMap<String, Object> parameter )
    {
        return ossSqlSessionTemplate.selectOne(statement, parameter);
    }

    public int delete( String statement, HashMap<String, Object> parameter )
    {
        return ossSqlSessionTemplate.delete( statement, parameter );
    }

    public int update( String statement, HashMap<String, Object> parameter )
    {
        int result = ossSqlSessionTemplate.update( statement, parameter );
        return result;
    }

    public int insert( String statement, HashMap<String, Object> parameter )
    {
        return ossSqlSessionTemplate.insert( statement, parameter );
    }

}
