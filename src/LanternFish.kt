class LanternFish (age: Int) {
    var age: Int

    init {
        this.age = age
    }

    constructor(): this(8)

    fun progress(): Boolean {
        return when {
            (--age < 0) -> {
                age = 6
                true
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return age
    }

    override fun equals(other: Any?) = (other is LanternFish) && age == other.age
}