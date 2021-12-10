package com.example.printertest

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

import android.util.DisplayMetrics
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    // I'm putting the variable here in case it is needed outside onCreate() later

    private lateinit var btPrint: BtPrint


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val printSwitch = findViewById<Switch>(R.id.printSwitch)
        val printInfo = findViewById<TextView>(R.id.printInfo)
        val printLoading = findViewById<ProgressBar>(R.id.printLoading)
        val transactionButton = findViewById<Button>(R.id.transactionButton)
        val edName = findViewById<EditText>(R.id.edName)



        btPrint = BtPrint( printSwitch, printLoading, printInfo, transactionButton )


        // Actual print

        transactionButton.setOnClickListener{


            // We do socket connect here so we can do some handling if something happen with the printer before
            // the actual printing.

            if(edName.equals(null) || edName.equals(" ") || edName.equals("")){
                Toast.makeText(applicationContext,"Mohon isikan Nama", Toast.LENGTH_SHORT)
            }else{
                btPrint.socketConnect { result ->

                    if ( result["success"] == false ){

                        this@MainActivity.runOnUiThread {

                            printInfo.text = result["text"].toString()
                            printSwitch.isChecked = false

                            Toast.makeText(this, "OOPS!!!", Toast.LENGTH_SHORT).show()

                            // TODO: Pooling?

                        }

                    } else {
                        // length maximal 32
                        var nama = edName.text
                        var inst = "Sekolah XXXXXXX"
                        var merc = "Kantin EDC"
                        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

//                    " ******** KATALIS EDC ******* \n" +
//                            comp + "\n"+
//                            prof + "\n"+
//                            " ____________________________ \n\n" +
//                            " Tanggal :" + this.today + "\n" +
//                            " Nama :" + this.name.toString().substr(0,20) + "\n" +
//                            " Trx.Kartu | " + this.getCurrency(nominal) + " \n" +
//                            " ------------------------------ \n" +
//                            " Total     | " + this.getCurrency(nominal) + " \n\n" +
//                            " STRUK INI SEBAGAI BUKTI \n" +
//                            " TRANSAKSI YANG SAH \n" +
//                            " MOHON DISIMPAN \n\n\n\n";
                        var stringToPrint = " ******** KATALIS EDC ******* \n" +
                                centerizeText(inst) + "\n" +
                                centerizeText(merc) + "\n" +
                                " ____________________________ \n\n" +
                                " Tanggal :" + sdf.format(Date()).toString() + "\n" +
                                " Nama :" + nama + "\n" +
                                " Trx.Kartu | " + "Rp 25.000" + " \n" +
                                " ------------------------------ \n" +
                                " Total     | " + "Rp 25.000" + " \n\n" +
                                centerizeText("STRUK INI SEBAGAI BUKTI") + "\n" +
                                centerizeText(" TRANSAKSI YANG SAH") +"\n" +
                                centerizeText("MOHON DISIMPAN") + "\n\n\n\n"


                        btPrint.doPrint( stringToPrint, false )
//                    btPrint.doPrint( android.os.Build.BRAND + "\n\n\n")


                        // I'll share how I handle printing format for regular receipts next... :)

                    }

                }
            }

        }
    }

    fun centerizeText(text:String): String {
        var trimmedText = text
        println(text.length)
        println(Math.floor(((30 - trimmedText.length)/2).toDouble()).toInt())
        var print = ""
        for(i in 0.. (Math.floor(((30 - trimmedText.length)/2).toDouble())).toInt()){
            print += " "
        }
        print += trimmedText
        return print
    }


}