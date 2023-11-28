package com.example.shimon.quizappintern

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.shimon.quizappintern.base.BaseFragment
import com.example.shimon.quizappintern.databinding.FragmentJavaPlayBinding
import java.util.concurrent.TimeUnit


class JavaPlayFragment : BaseFragment<FragmentJavaPlayBinding>(FragmentJavaPlayBinding::inflate) {
    var quizList = listOf<Quiz>(

        Quiz(
            "What is the entry point method in a Java program?",
            "start",
            "main()",
            "execute()",
            "begin()",
            "main()"
        ),
        Quiz(
            "Which keyword is used to inherit a class in Java?",
            "superclass",
            "extends",
            "implements",
            "inherits",
            "extends"
        ),
        Quiz(
            "What is the default value of an instance variable of a class in Java (if not explicitly initialized)",
            "0",
            "1",
            "null",
            "Depends on the data type",
            "Depends on the data type"
        ),

        Quiz(
            "Which of the following Java modifiers can be used with a class?",
            "protected",
            "private",
            "public",
            "All of the above",
            "All of the above"
        ),

        Quiz(
            "Which loop is guaranteed to execute at least once in Java?",
            "if",
            "do-while",
            "while",
            "for",
            "do-while"
        ),

        Quiz(
            "What is the purpose of the static keyword in Java?",
            " It makes methods execute asynchronously.",
            "It allows access to the class members without creating an object of the class.",
            "It creates an instance of the class.",
            " It makes variables constant and unchangeable.\n",
            "It allows access to the class members without creating an object of the class."
        ),

        Quiz(
            "Which data structure uses a Last-In-First-Out (LIFO) order?",
            "Stack",
            "LinkedList",
            "ArrayList",
            "Queue",
            "Stack"
        ),


        Quiz(
            "What does the final keyword do in Java?",
            " Prevents variable reassignment",
            " Prevents method overriding",
            "Prevents inheritance",
            "All of the above",
            "All of the above"
        ),

        Quiz(
            "Which statement is used to exit from a loop in Java?",
            "terminate",
            "exit",
            "break",
            "stop",
            "break"
        ),

        Quiz(
            "Which keyword is used to define a constant in Java?",
            "static",
            "define",
            "final",
            "const",
            "final"
        )


        )

    var updateQuestionNo = 1
    var countDownTimer: CountDownTimer? = null
    var miliLestSec = 30000L
    var index = 0
    var hasFinished = false
    var skip = -1
    var correct = 0
    var wrong = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.questionNumberTV.text = "$updateQuestionNo/${quizList.size}"
        startCountDownTimer()
        initQuestion()

        binding.nextQuestionBTN.setOnClickListener {
            showNextQuestion()
        }



    }

    private fun startCountDownTimer(){
        countDownTimer = object: CountDownTimer(miliLestSec,1000){
            override fun onTick(millisUntilFinished: Long) {
                miliLestSec = millisUntilFinished
                val second = TimeUnit.MILLISECONDS.toSeconds(miliLestSec).toInt()

                binding.timerTV.text = "Time Left : $second"

            }

            override fun onFinish() {
                showNextQuestion()
            }

        }.start()
    }

    private fun initQuestion() {
        val quiz = quizList[index]

        binding.apply {
            questionTV.text = quiz.question
            option1.text =quiz.option1
            option2.text = quiz.option2
            option3.text = quiz.option3
            option4.text = quiz.option4
        }


    }
    private fun showNextQuestion(){
        checkAnswer()
        binding.apply {
            //question update after clicking next BTN
            if (updateQuestionNo<quizList.size){
                updateQuestionNo++
                questionNumberTV.text = "$updateQuestionNo/${quizList.size}"
            }
            if (index<=quizList.size-1){

                initQuestion()
            }
            //when all question was finished then this function will work
            else{
                hasFinished = true
                Toast.makeText(requireContext(), "goto result", Toast.LENGTH_SHORT).show()
            }
//clear all ans when new question was came
            radioGroup.clearCheck()
        }

    }

    private fun checkAnswer() {

        binding.apply {

            if ( radioGroup.checkedRadioButtonId == -1) {
                skip++
            }
            else{
                val checkButton = view?.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                var checkAnswer = checkButton?.text.toString()

                if (checkAnswer==quizList[index].rightAnswer){
                    correct++
                    scoreTV.text = "Score : $correct"
                    countDownTimer?.cancel()
                    showAlertDialog("Right Answer")
                }
                else{
                    wrong++
                    countDownTimer?.cancel()
                    showAlertDialog("Wrong Answer")
                }

            }
            if (index <=quizList.size-1){
                index++
            }
            else{
                showAlertDialog("Finish")
            }



        }
    }

    fun showAlertDialog(message : String){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(message)

        builder.setPositiveButton("ok", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (message=="Finish"){
                    countDownTimer?.cancel()


                    val bundle = Bundle()
                    bundle.putString("skip", skip.toString())
                    bundle.putString("right",correct.toString())
                    bundle.putString("wrong", wrong.toString())
                    bundle.putString("numOfQuestion", quizList.size.toString())
                    findNavController().navigate(R.id.action_javaPlayFragment_to_resultFragment,bundle)

                }

                countDownTimer?.start()
            }

        })

        var alertDialog = builder.create()
        alertDialog.show()


    }



}