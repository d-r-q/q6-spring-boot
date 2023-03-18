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


class CustomStringArrayType : UserType<Any> {

    fun sqlTypes(): IntArray {
        return intArrayOf(Types.ARRAY)
    }

    override fun equals(x: Any?, y: Any?): Boolean {
        return x == y
    }

    override fun hashCode(x: Any?): Int {
        return x!!.hashCode()
    }

    override fun getSqlType(): Int {
        return Types.ARRAY
    }

    override fun returnedClass(): Class<Any> {
        return Any::class.java
    }

    override fun nullSafeGet(
        rs: ResultSet,
        position: Int,
        session: SharedSessionContractImplementor?,
        owner: Any?
    ): Any {
        val array = rs.getArray(position)
        return (array?.array as Array<String>).map { Role.valueOf(it) }.toTypedArray()
    }

    override fun isMutable(): Boolean {
        return false
    }

    override fun assemble(cached: Serializable?, owner: Any?): Any? {
        return cached
    }

    override fun replace(detached: Any?, managed: Any?, owner: Any?): Any? {
        return detached
    }

    override fun disassemble(value: Any?): Serializable {
        return value as Serializable
    }

    override fun deepCopy(value: Any?): Any? {
        val arr = value!! as Array<Any>
        return Arrays.copyOf(arr, arr.size);
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor
    ) {
        if (value != null) {
            val array: java.sql.Array? =
                st.connection.createArrayOf("text", (value as Array<Enum<*>>).map { it.name }.toTypedArray())
            st.setArray(index, array)
        } else {
            st.setNull(index, sqlTypes()[0])
        }
    }
}