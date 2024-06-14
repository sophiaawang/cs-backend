package org.cuair.cloud.util

object MatrixUtil {
    fun multiply(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>): Array<DoubleArray> {
        val r1 = firstMatrix.size
        val c1 = secondMatrix.size
        val c2 = secondMatrix[0].size
        val product = Array(r1) { DoubleArray(c2) }
        for (i in 0 until r1) {
            for (j in 0 until c2) {
                for (k in 0 until c1) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j]
                }
            }
        }
        return product
    }

    // only works for square matrices
    fun transpose(A: Array<DoubleArray>): Array<DoubleArray> {
        for (i in A.indices) for (j in i + 1 until A[i].size) {
            val temp = A[i][j]
            A[i][j] = A[j][i]
            A[j][i] = temp
        }
        return A
    }

    fun scaleMultiplyVector(vec: DoubleArray, scale: Double): DoubleArray {
        for (i in vec.indices) {
            vec[i] = scale * vec[i]
        }
        return vec
    }

    fun vecFromArray(arr: DoubleArray): Array<DoubleArray> {
        val vec = Array(arr.size) { DoubleArray(1) }
        for (i in arr.indices) {
            vec[i][0] = arr[i]
        }
        return vec
    }

    fun arrFromVec(vec: Array<DoubleArray>): DoubleArray {
        val arr = DoubleArray(vec.size)
        for (i in vec.indices) {
            arr[i] = vec[i][0]
        }
        return arr
    }
}