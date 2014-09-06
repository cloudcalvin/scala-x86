package com.scalaAsm.x86
package Instructions
package Standard

trait SUB extends x86Instruction {
  val mnemonic = "SUB"
}

trait SUB_2[-O1, -O2, OpEn <: TwoOperandEncoding[O1, O2]] extends TwoOperandInstruction[O1, O2, OpEn, OneOpcode] with SUB

object SUB {

  implicit object sub1 extends SUB_2[rm32, imm8, MI] {
    def opcode = 0x83 /+ 5
  }

  implicit object sub3 extends SUB_2[rm64, imm8, MI] {
    def opcode = 0x83 /+ 5
  }

  implicit object sub2 extends SUB_2[r32, rm32, RM] {
    def opcode = 0x2B
  }
}