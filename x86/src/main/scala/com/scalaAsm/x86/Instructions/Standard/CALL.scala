package com.scalaAsm.x86
package Instructions
package Standard

abstract class CALL_1[-O1, OpEn <: OneOperandEncoding[O1]] extends OneOperandInstruction[O1, OpEn, OneOpcode]("CALL")

trait CALLLow {
  implicit object call2 extends CALL_1[rm32, M] {
    def opcode = 0xFF /+ 2
  }
}

object CALL_1 extends CALLLow {

  implicit object call3 extends CALL_1[rel32, M] {
    def opcode = 0xE8
  }

  implicit object call1 extends CALL_1[rm16, M] {
    def opcode = 0xFF /+ 2
  }
}