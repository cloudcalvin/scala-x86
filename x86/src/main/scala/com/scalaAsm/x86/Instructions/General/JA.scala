package com.scalaAsm.x86
package Instructions
package General

import com.scalaAsm.x86.Operands._
import com.scalaAsm.x86.Operands.Memory._

// Description: Jump short if not below or equal/above (CF=0 AND ZF=0)
// Category: general/branch/cond

object JA extends InstructionDefinition[OneOpcode]("JA") with JAImpl

trait JAImpl {
  implicit object JA_0 extends JA._1[rel8] {
    def opcode = 0x77
  }
}