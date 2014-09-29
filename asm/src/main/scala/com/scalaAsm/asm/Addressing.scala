package com.scalaAsm.asm

import com.scalaAsm.x86.Operands._
import com.scalaAsm.x86.Operands.Memory.AbsoluteAddress
import com.scalaAsm.x86.Operands.Memory.AddressingMode
import com.scalaAsm.x86.Operands.Memory.AbsoluteAddress
import com.scalaAsm.x86.`package`.OperandSize

trait Addressing {
  def *[A <: GPR, B <: OperandSize](mem: A#BaseIndex[B]): Operand[_,A#BaseIndex[B]] = Op(mem)

  def *[C <: OperandSize](offset: Constant[C])(implicit abs: AbsoluteAddress[C]): AbsoluteAddress[C] = { abs.offset = offset.value; abs }

  def *[X <: OperandSize](gpr: GeneralPurpose[X]) = Op(new gpr.Indirect {})
  
}