package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class MemoizedTest: AbstractSpekTestEngineTest() {
    @Test
    fun memoizedTestCaching() {
        class MemoizedSpec: Spek({
            val foo = memoized {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            test("first pass") {
                memoized1 = foo()
            }

            test("second pass") {
                memoized2 = foo()
            }

            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedGroupCaching() {
        class MemoizedSpec: Spek({
            val foo = memoized(CachingMode.GROUP) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo()
                }
            }

            group("another group") {
                test("second pass") {
                    memoized2 = foo()
                }

            }


            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedNestedGroupCaching() {
        class MemoizedSpec: Spek({
            val foo = memoized(CachingMode.GROUP) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo()
                }

                group("another group") {
                    test("second pass") {
                        memoized2 = foo()
                    }

                }
            }

            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedScopeCaching() {
        class MemoizedSpec: Spek({
            val foo = memoized(CachingMode.SCOPE) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo()
                }
            }

            group("another group") {
                test("second pass") {
                    memoized2 = foo()
                }

            }


            test("check") {
                assertThat(memoized1, sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedScopeCachingWithNestedGroups() {
        class MemoizedSpec: Spek({
            val foo = memoized(CachingMode.SCOPE) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo()
                }

                group("another group") {
                    test("second pass") {
                        memoized2 = foo()
                    }

                }
            }

            test("check") {
                assertThat(memoized1, sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }


    @Test
    fun memoizedActionCaching() {
        class MemoizedSpec: Spek({
            val foo = memoized {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            action("some action") {
                test("first pass") {
                    memoized1 = foo()
                }

                test("second pass") {
                    memoized2 = foo()
                }

                test("check") {
                    assertThat(memoized1, sameInstance(memoized2))
                }
            }


        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }
}
