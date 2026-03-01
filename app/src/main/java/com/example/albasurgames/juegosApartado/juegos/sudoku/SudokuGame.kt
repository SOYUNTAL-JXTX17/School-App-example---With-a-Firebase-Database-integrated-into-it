package com.example.albasurgames.juegosApartado.juegos.sudoku

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<CellInt>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false

    private val board: Board

    init {
        val cells = List(9 * 9) { i -> CellInt(i / 9, i % 9, i % 9) }
        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: String) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board.getCell(selectedRow, selectedCol)

        if (!isTakingNotes) cell.value = number.toInt()
        cellsLiveData.postValue(board.cells)
    }


    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }

    fun changeNoteTakingState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = setOf<Int>()
        highlightedKeysLiveData.postValue(curNotes)
    }

    fun delete()
    {
        if (selectedRow == -1 || selectedCol == -1) return
        if (!isTakingNotes)
        {
            val cell = board.getCell(selectedRow, selectedCol)
            cell.value = 0
            cellsLiveData.postValue(board.cells)
        }
    }
}
