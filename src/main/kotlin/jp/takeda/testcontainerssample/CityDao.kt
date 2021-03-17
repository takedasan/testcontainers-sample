package jp.takeda.testcontainerssample

import org.seasar.doma.Dao
import org.seasar.doma.Select
import org.seasar.doma.Sql
import org.seasar.doma.boot.ConfigAutowireable

@Dao
@ConfigAutowireable
interface CityDao {
    @Sql("select * from city")
    @Select
    fun selectAll(): List<City>
}
