package com.example.miinstitutoapp.juegosApartado.juegos.sudoku


import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.miinstitutoapp.R

class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var numberButtons: List<AppCompatButton>

    private lateinit var sudokuBoardView: SudokuBoardView
    private lateinit var notesButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var oneButton: AppCompatButton
    private lateinit var twoButton: AppCompatButton
    private lateinit var threeButton: AppCompatButton
    private lateinit var fourButton: AppCompatButton
    private lateinit var fiveButton: AppCompatButton
    private lateinit var sixButton: AppCompatButton
    private lateinit var sevenButton: AppCompatButton
    private lateinit var eightButton: AppCompatButton
    private lateinit var nineButton: AppCompatButton

    private lateinit var backButton: LinearLayout

    private lateinit var sharedPreferences: SharedPreferences
    private var modoAnadido: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContentView(R.layout.activity_play_sudoku)

        initComponents()

        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer { updateNoteTakingUI(it) })

        numberButtons = listOf(oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton,
            sevenButton, eightButton, nineButton)

        numberButtons.forEach { button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(button.text.toString())
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        notesButton.setOnClickListener { viewModel.sudokuGame.changeNoteTakingState() }
        deleteButton.setOnClickListener { viewModel.sudokuGame.delete() }
    }

    private fun initComponents() {
        sudokuBoardView = findViewById(R.id.sudokuBoardView)
        notesButton = findViewById(R.id.notesButton)
        deleteButton = findViewById(R.id.deleteButton)
        oneButton = findViewById(R.id.oneButton)
        twoButton = findViewById(R.id.twoButton)
        threeButton = findViewById(R.id.threeButton)
        fourButton = findViewById(R.id.fourButton)
        fiveButton = findViewById(R.id.fiveButton)
        sixButton = findViewById(R.id.sixButton)
        sevenButton = findViewById(R.id.sevenButton)
        eightButton = findViewById(R.id.eightButton)
        nineButton = findViewById(R.id.nineButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun updateCells(cells: List<CellInt>?) = cells?.let {
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateNoteTakingUI(isNoteTaking: Boolean?) = isNoteTaking?.let {
        val color = if (it) ContextCompat.getColor(this, R.color.colorPrimary)
        else
        {
            sharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)
            modoAnadido = sharedPreferences.getBoolean("modoOscuro", false)

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
                || (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                || modoAnadido)
            {
                ContextCompat.getColor(this, R.color.white)
            }
            else ContextCompat.getColor(this, R.color.black)
        }
        notesButton.setBackgroundColor(color)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}