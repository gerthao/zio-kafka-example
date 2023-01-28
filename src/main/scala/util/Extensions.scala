package util

object Extensions {
  extension[T] (x: T)
    def |>[U] (f: T => U): U =  f(x)
    def \> (f: T => Unit): T =
      f(x)
      x
}
