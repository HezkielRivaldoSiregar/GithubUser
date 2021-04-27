package com.dicoding.githubuser.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.dicoding.githubuser.receiver.AlarmReceiver
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityAlarmBinding
import com.dicoding.githubuser.fragment.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity(), View.OnClickListener,
    TimePickerFragment.DialogTimeListener {
    private var binding: ActivityAlarmBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)
        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRepeatingTime -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_setRepeatingAlarm -> {
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                val repeatMessage = binding?.edtRepeatingMessage?.text.toString()
                alarmReceiver.setRepeatingAlarm(this, repeatTime, repeatMessage)
            }
            R.id.btn_cancelRepeatingAlarm -> alarmReceiver.cancelAlarm(this)
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            TIME_PICKER_REPEAT_TAG -> binding?.tvRepeatingTime?.text =
                dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}