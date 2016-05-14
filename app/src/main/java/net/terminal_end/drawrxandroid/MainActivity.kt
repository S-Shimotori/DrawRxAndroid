package net.terminal_end.drawrxandroid

import android.os.Bundle
import android.os.HandlerThread
import android.os.Looper
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.support.v7.app.AppCompatActivity
import android.util.Log
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.exceptions.OnErrorThrowable
import rx.lang.kotlin.fold
import rx.lang.kotlin.observable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var backgroundLooper: Looper
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backgroundThread: BackgroundThread = BackgroundThread()
        backgroundThread.start();
        backgroundLooper = backgroundThread.looper

        findViewById(R.id.button_run_scheduler)!!.setOnClickListener {
            onRunSchedulerExampleButtonClicked()
        }
        /*
        SAM conversions
        findViewById(R.id.button_run_scheduler)!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View) {
                onRunSchedulerExampleButtonClicked()
            }
        })
         */

        observable<String> { subscriber ->
            subscriber.onNext("H")
            subscriber.onNext("e")
            subscriber.onNext("l")
            subscriber.onNext("")
            subscriber.onNext("l")
            subscriber.onNext("o")
            subscriber.onCompleted()
        }.filter { it.isNotEmpty() }
                .fold (StringBuilder()) { sb, e -> sb.append(e) }
                .map { it.toString() }
                .subscribe { Log.d(tag, it) }
    }

    fun onRunSchedulerExampleButtonClicked() {
        sampleObservable()
                // Run on a background thread
                .subscribeOn(AndroidSchedulers.from(backgroundLooper))
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<String>() {
                    override fun onCompleted() {
                        Log.d(tag, "onCompleted()")
                    }

                    override fun onError(e: Throwable ) {
                        Log.e(tag, "onError()", e)
                    }

                    override fun onNext(string: String) {
                        Log.d(tag, "onNext($string)")
                    }
                })
    }

    companion object {

        fun  sampleObservable(): Observable<String> {
            /*
            SAM conversions
            return Observable.defer(object: Func0<Observable<String>>) {
                override fun call(): Observable<String> {
                ...
             */
            return Observable.defer {
                try {
                    // Do some long running operation
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5))
                } catch (e: InterruptedException) {
                    throw OnErrorThrowable.from(e)
                }
                Observable.just("one", "two", "three", "four", "five")
            }
        }

        class BackgroundThread: HandlerThread("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND) {
        }
    }

}
