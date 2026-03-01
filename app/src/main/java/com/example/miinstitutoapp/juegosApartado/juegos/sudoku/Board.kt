package com.example.miinstitutoapp.juegosApartado.juegos.sudoku

class Board(val size: Int, val cells: List<CellInt>) {
    fun getCell(row: Int, col: Int) = cells[row * size + col]
}