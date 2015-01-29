package com.scalaAsm.x86
package Instructions
package General

import com.scalaAsm.x86.Operands._
import com.scalaAsm.x86.Operands.Memory._

object INSW extends InstructionDefinition[OneOpcode]("INSW") with INSWImpl

// Input from Port to String

trait INSWImpl {
  implicit object INSW_0 extends INSW._0 {
    def opcode = 0x6D
    override def hasImplicitOperand = true
  }
}
