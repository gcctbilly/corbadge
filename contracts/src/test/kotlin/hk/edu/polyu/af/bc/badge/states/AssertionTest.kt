package hk.edu.polyu.af.bc.badge.states

import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import net.corda.testing.core.TestIdentity
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class AssertionTest {
    private val me: Party = TestIdentity.fresh("AFBlockchain").party
    private val badgeClass = BadgeClass("A Badge", "Cool", ByteArray(1),me, UniqueIdentifier())

    @Test
    fun `can create an assertion`() {
        val assertion = Assertion(badgeClass.toPointer(), me, me, Date(1,1,1),true,UniqueIdentifier())

        assertEquals(assertion.holder, me)
        assertEquals(assertion.issuer, me)
        assertEquals(assertion.issuedTokenType.tokenType.tokenClass, BadgeClass::class.java)
    }

    @Test
    fun `should print correct date string representation`() {
        val dateString = Assertion.assertionDateFormat.format(Date(122, 0, 12))

        assert(dateString == "12.01.2022")
    }

    @Test
    fun `can parse date string`() {
        val date = Assertion.assertionDateFormat.parse("12.01.2022")

        assert(date.year == 122 && date.month == 0 && date.date == 12)
    }
}