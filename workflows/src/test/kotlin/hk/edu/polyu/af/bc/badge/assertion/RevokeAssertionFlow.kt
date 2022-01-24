package hk.edu.polyu.af.bc.badge.assertion

import hk.edu.polyu.af.bc.badge.UnitTestBase
import hk.edu.polyu.af.bc.badge.badgeclass.CreateBadgeClassTest
import hk.edu.polyu.af.bc.badge.flows.assertion.IssueAssertion
import hk.edu.polyu.af.bc.badge.flows.assertion.RevokeAssertionFlow
import hk.edu.polyu.af.bc.badge.flows.badgeclass.CreateBadgeClass
import hk.edu.polyu.af.bc.badge.getOrThrow
import hk.edu.polyu.af.bc.badge.output
import hk.edu.polyu.af.bc.badge.states.Assertion
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevokeAssertionFlowTest: UnitTestBase() {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(CreateBadgeClassTest::class.java)
    }

    @Test
    fun `can revoke assertion`() {
        // create a BadgeClass
        logger.info("Creating BadgeClass");
        val badgeClassTx = instA.startFlow(CreateBadgeClass("test", "test", ByteArray(1))).getOrThrow(network)
        val badgeClass = badgeClassTx.output(BadgeClass::class.java)
        logger.info("BadgeClass Created: {}", badgeClass);

        // issue an Assertion
        logger.info("Issuing assertion")
        val assertionTx = instA.startFlow(IssueAssertion(badgeClass.toPointer(), learnerA.info.legalIdentities[0])).getOrThrow(network)
        val assertion = assertionTx.output(Assertion::class.java)
        val uuid=assertion.linearId.id
        logger.info("Assertion issued: {}", assertion.toString())
        //revoke the assertion
        instA.startFlow(RevokeAssertionFlow(uuid)).getOrThrow(network)

        assertTrue(assertion.revoked)

    }
}