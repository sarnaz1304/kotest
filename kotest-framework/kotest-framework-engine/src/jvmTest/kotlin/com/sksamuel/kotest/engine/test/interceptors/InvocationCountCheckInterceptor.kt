package com.sksamuel.kotest.engine.test.interceptors

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.descriptors.append
import io.kotest.core.descriptors.toDescriptor
import io.kotest.core.names.TestName
import io.kotest.core.sourceRef
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.engine.test.scopes.NoopTestScope
import io.kotest.engine.test.interceptors.InvocationCountCheckInterceptor
import io.kotest.matchers.booleans.shouldBeTrue
import kotlin.time.milliseconds

class InvocationCountCheckInterceptorTest : DescribeSpec() {
   init {
      describe("InvocationCountCheckInterceptor") {

         it("should invoke downstream if invocation count == 1 for containers") {
            val tc = TestCase(
               InvocationCountCheckInterceptorTest::class.toDescriptor().append("foo"),
               TestName("foo"),
               InvocationCountCheckInterceptorTest(),
               {},
               sourceRef(),
               TestType.Container,
            )
            var fired = false
            InvocationCountCheckInterceptor.intercept { _, _ ->
               fired = true
               TestResult.Success(0.milliseconds)
            }.invoke(tc.copy(config = tc.config.copy(invocations = 1)), NoopTestScope(tc, coroutineContext))
            fired.shouldBeTrue()
         }

         it("should invoke downstream if invocation count > 1 for tests") {
            val tc = TestCase(
               InvocationCountCheckInterceptorTest::class.toDescriptor().append("foo"),
               TestName("foo"),
               InvocationCountCheckInterceptorTest(),
               {},
               sourceRef(),
               TestType.Test,
            )
            var fired = false
            InvocationCountCheckInterceptor.intercept { _, _ ->
               fired = true
               TestResult.Success(0.milliseconds)
            }.invoke(tc.copy(config = tc.config.copy(invocations = 44)), NoopTestScope(tc, coroutineContext))
            fired.shouldBeTrue()
         }

         it("should error if invocation count > 1 for containers") {
            val tc = TestCase(
               InvocationCountCheckInterceptorTest::class.toDescriptor().append("foo"),
               TestName("foo"),
               InvocationCountCheckInterceptorTest(),
               {},
               sourceRef(),
               TestType.Container,
            )
            shouldThrowAny {
               InvocationCountCheckInterceptor.intercept { _, _ -> TestResult.Success(0.milliseconds) }
                  .invoke(tc.copy(config = tc.config.copy(invocations = 4)), NoopTestScope(tc, coroutineContext))
            }
         }
      }
   }
}
