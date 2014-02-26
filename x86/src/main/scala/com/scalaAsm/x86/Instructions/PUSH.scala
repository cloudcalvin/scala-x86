package com.scalaAsm.x86.Instructions

import com.scalaAsm.x86.Operands._
import com.scalaAsm.x86.OneOperand
import com.scalaAsm.x86.OperandEncoding
import com.scalaAsm.x86.{OpcodePlusRd, ModRM, x86Instruction, OperandSize, Opcodes, OneOpcode, Immediate, DwordOperand, WordOperand}
import com.scalaAsm.x86.AddressingFormSpecifier
import com.scalaAsm.x86.x86Registers._
import scala.annotation.implicitNotFound
import com.scalaAsm.x86.Instruction

abstract class PUSH extends x86Instruction("PUSH")

@implicitNotFound(msg = "Cannot find PUSH implementation for ${O1}")
trait PUSH_1[-O1] extends PUSH with OneOperand[O1] with OperandEncoding

trait POWLow {
    
  implicit object push6 extends PUSH_1[rm32] {
      def operands = M(x)
      val opcode = 0xFF /+ 6
  }
}

object PUSH extends POWLow {
  
  implicit object push1 extends PUSH_1[r32] {
      def operands = O(x)
      def opcode = OpcodePlusRd(0x50, x)
  }
  
  implicit object push8 extends PUSH_1[r16] {
      def operands = O(x)
      def opcode = OpcodePlusRd(0x50, x)
  }
  
  implicit object push4 extends PUSH_1[imm8] {
      def operands = I[imm8](x)
      val opcode: Opcodes = 0x6A
  }
  
  implicit object push5 extends PUSH_1[imm16] {
      def operands = I[imm16](x)
      val opcode: Opcodes = 0x68
  }
  
  implicit object push7 extends PUSH_1[CS] {
      def operands = new OneOperand[CS](x) {def getAddressingForm = null}
      val opcode: Opcodes = 0x0E
  }
}