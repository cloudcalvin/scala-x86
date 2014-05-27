package com.scalaAsm.x86
package Instructions

abstract class MOV extends x86Instruction("MOV")

trait MOV_2[-O1 <: Operand, -O2 <: Operand] extends MOV with TwoOperandInstruction[O1,O2]

trait MOVLow {
  
 implicit object mov1 extends MOV_2[rm32, r32] {
      def opEn = MR
      val opcode = OneOpcode(0x89)
  }
}

object MOV extends MOVLow {
 
  implicit object mov3 extends MOV_2[r32, rm32] {
      def opEn = RM
      val opcode = OneOpcode(0x8B)
  }
  
  implicit object mov9 extends MOV_2[r16, rm16] {
      def opEn = RM
      val opcode = OneOpcode(0x8B)
  }
  
  implicit object mov7 extends MOV_2[r16, imm16] {
      def opEn = OI
      val opcode = OpcodePlusRd(0xB8)
  }
  
  implicit object mov8 extends MOV_2[r8, imm8] {
      def opEn = OI
      val opcode = OpcodePlusRd(0xB0)
  }
  
  implicit object mov6 extends MOV_2[r32, imm32] {
      def opEn = OI
      val opcode = OpcodePlusRd(0xB8)
  }
   
  implicit object mov10 extends MOV_2[r64, imm64] {
      def opEn = OI
      val opcode = OpcodePlusRd(0xB8)
  }
  
  implicit object mov11 extends MOV_2[rm64, r64] {
      def opEn = MR
      val opcode = OneOpcode(0x89)
  }
}