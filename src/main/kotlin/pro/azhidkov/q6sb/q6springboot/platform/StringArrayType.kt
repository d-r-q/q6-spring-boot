package pro.azhidkov.q6sb.q6springboot.platform

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import pro.azhidkov.q6sb.q6springboot.domain.Role
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.util.*


class StringArrayType : UserType<Array<String>> {

    fun sqlTypes(): IntArray {
        return intArrayOf(Types.ARRAY)
    }

    override fun equals(x: Array<String>?, y: Array<String>?): Boolean {
        return x.contentEquals(y)
    }

    override fun hashCode(x: Array<String>?): Int {
        return x!!.hashCode()
    }

    override fun getSqlType(): Int {
        return Types.ARRAY
    }

    override fun returnedClass(): Class<Array<String>> {
        return Array<String>::class.java
    }

    override fun nullSafeGet(
        rs: ResultSet,
        position: Int,
        session: SharedSessionContractImplementor?,
        owner: Any?
    ): Array<String> {
        val array = rs.getArray(position)
        return (array?.array as Array<String>)
    }

    override fun isMutable(): Boolean {
        return false
    }

    override fun assemble(cached: Serializable?, owner: Any?): Array<String>? {
        return cached as Array<String>?
    }

    override fun replace(detached: Array<String>?, managed: Array<String>?, owner: Any?): Array<String>? {
        return detached
    }

    override fun disassemble(value: Array<String>?): Serializable {
        return value as Serializable
    }

    override fun deepCopy(value: Array<String>?): Array<String>? {
        val arr = value!! as Array<String>
        return Arrays.copyOf(arr, arr.size)
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        st: PreparedStatement,
        value: Array<String>?,
        index: Int,
        session: SharedSessionContractImplementor
    ) {
        if (value != null) {
            val array: java.sql.Array? =
                st.connection.createArrayOf("text", (value))
            st.setArray(index, array)
        } else {
            st.setNull(index, sqlTypes()[0])
        }
    }
}