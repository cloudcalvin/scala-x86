package com.scalaAsm.x86
package Instructions
package Standard

import com.scalaAsm.x86.Operands._
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find PUSH implementation for ${O1}")
abstract class PUSH_1[-O1, OpEn <: OneOperandEncoding[O1]] extends OneOperandInstruction[O1, OpEn, OneOpcode]("PUSH")

trait POWLow {
    
  implicit object LowPush extends PUSH_1[rm, M] {
      def opcode = 0xFF /+ 6
  }
  
  implicit object push3 extends PUSH_1[r16, O] {
      def opcode = 0x50 + rw
  }
}

object PUSH_1 extends POWLow {

  implicit object push1 extends PUSH_1[r64, O] {
      def opcode = 0x50 + rd
      override val defaultsTo64Bit = true
  }
  
  implicit object push2 extends PUSH_1[r32, O] {
      def opcode = 0x50 + rd 
  }
  
  implicit object push4 extends PUSH_1[imm8, I] {
      def opcode = 0x6A
  }
  
  implicit object push5 extends PUSH_1[imm16, I] {
      def opcode = 0x68
  }
  
  implicit object push6 extends PUSH_1[imm32, I] {
      def opcode = 0x68
  }
  
  implicit object push7 extends PUSH_1[CS, CSFormat] {
      def opcode = 0x0E
  }
}