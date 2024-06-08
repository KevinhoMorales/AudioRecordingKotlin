package com.kevinhomorales.audiorecordingkotlin

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kevinhomorales.audiorecordingkotlin.databinding.ActivityMainBinding
import java.io.IOException
import android.Manifest
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mediaRecorder: MediaRecorder
    lateinit var mediaPlayer: MediaPlayer
    lateinit var audioSavaPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        // Solicitud de permiso de micrófono y escritura de almacenamiento externa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), 1)
        }
        setUpActions()
    }

    private fun setUpActions() {
        binding.startRecordingButton.setOnClickListener {
            audioSavaPath = getFilesDir().absolutePath + "/" + "recordingAudio.mp3"
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder.setOutputFile(audioSavaPath)
            try {
                mediaRecorder.prepare()
                mediaRecorder.start()
                Toast.makeText(this, "Grabación guardada en $audioSavaPath", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        binding.stopRecordingButton.setOnClickListener {
            mediaRecorder.stop()
            mediaRecorder.release()
            Toast.makeText(this, "Grabación parada", Toast.LENGTH_SHORT).show()
        }

        binding.startPlayingButton.setOnClickListener {
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(audioSavaPath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                Toast.makeText(this, "Esuchando a grabación", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        binding.stopPlayinhButton.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            Toast.makeText(this, "Se paró la grabación", Toast.LENGTH_SHORT).show()
        }
    }
}