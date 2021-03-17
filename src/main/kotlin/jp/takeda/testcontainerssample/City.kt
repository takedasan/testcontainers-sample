package jp.takeda.testcontainerssample

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Table

@Entity
@Table(name = "CITY")
data class City(
    @Id
    var id: Int = -1,
    var name: String = ""
)
