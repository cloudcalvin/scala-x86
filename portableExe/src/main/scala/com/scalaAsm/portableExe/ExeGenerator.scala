package com.scalaAsm.portableExe

import java.io.{ OutputStream }
import java.nio.{ ByteBuffer, ByteOrder }
import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap
import java.io.DataOutputStream
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.ListBuffer
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import com.scalaAsm.asm.Assembled
import com.scalaAsm.asm.AsmCompiler
import com.scalaAsm.utils.Endian
import java.io.File
import java.io.FileInputStream
import com.scalaAsm.asm.Tokens.ImportRef
import sections._
  
private[portableExe] case class CompiledImports(rawData: Array[Byte],
                            boundImportSize: Int,
                            nameTableSize: Int,
                            imports: Map[String, Int]) {
  
  def getImportsDirectory(addressOfData: Int, dataSize: Int) = {
    val importsLocation = addressOfData + dataSize
    ImageDataDirectory(importsLocation, boundImportSize)
  }
  
  def getIATDirectory(addressOfData: Int, dataSize: Int) = {
    val endOfData = addressOfData + dataSize
    val IATlocation = endOfData + boundImportSize + nameTableSize
    ImageDataDirectory(IATlocation, nameTableSize)
  }  
}

object ExeGenerator {
  
  def align(array: Array[Byte], to: Int, filler: Byte = 0xCC.toByte) = {
    val currentSize = array.size
    val numPadding = (to - (currentSize % to)) % to
    array ++ Array.fill(numPadding)(filler)
  }
  
  private def compileImports(addressOfData: Int, dataSize: Int, dlls: Seq[String], possibleFunctions: Seq[String], is64Bit: Boolean): CompiledImports = { 
    
    val dllImports = dlls flatMap { dll =>
	    val file = new File("C:/Windows/System32/" + dll);
	 
	    val bFile: Array[Byte] = Array.fill(file.length().toInt)(0);
	      
	    //convert file into array of bytes
	    val fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    
	    val bbuf = ByteBuffer.wrap(bFile)
	    bbuf.order(ByteOrder.LITTLE_ENDIAN)
	    
	    val dosHeader = DosHeader.getDosHeader(bbuf)
	    val peHeader = PeHeader.getPeHeader(bbuf)
	    val dirs = DataDirectories.getDirectories(bbuf)
	    val sections = Sections.getSections(bbuf, peHeader.fileHeader.numberOfSections)
	
	    val export = ImageExportDirectory.getExports(bbuf, sections, dirs.exportSymbols)
	    val importedSymbols = export.functionNames intersect possibleFunctions

	    if (importedSymbols.isEmpty)
	      None
	    else
	      Some(Extern(dll, importedSymbols))
    }
    
    val test = Imports(imports = dllImports,
                       offset = addressOfData + dataSize)

    test.generateImports(is64Bit)
  }
  
  //works
//  def compileResources(beginningOfSection: Int): Array[Byte] = { 
//    
//    val file = new File("testicon.ico");
//	 
//    
//    val bFile: Array[Byte] = Array.fill(file.length().toInt)(0);
//    
//    val buffer = ByteBuffer.allocate(21300)
//    buffer.order(ByteOrder.LITTLE_ENDIAN)
//    
//    // ROOT
//    buffer.putInt(0) // characteristics
//    buffer.putInt(0) // time
//    buffer.putShort(0) // major version
//    buffer.putShort(0) // minor version
//    buffer.putShort(0) // num named
//    buffer.putShort(1) // num id
//    
//    buffer.putInt(DirectoryTypeID.groupIcon.id) // name
//    buffer.putInt(0x80000000 + 16 + 8) // offsetToData
//    
//    buffer.putInt(0) // characteristics
//    buffer.putInt(0) // time
//    buffer.putShort(0) // major version
//    buffer.putShort(0) // minor version
//    buffer.putShort(0) // num named
//    buffer.putShort(1) // num id
//    
//    buffer.putInt(3)
//    buffer.putInt(0x80000000 + 16 + 8 + 16 + 8)
//    
//    buffer.putInt(0) // characteristics
//    buffer.putInt(0) // time
//    buffer.putShort(0) // major version
//    buffer.putShort(0) // minor version
//    buffer.putShort(0) // num named
//    buffer.putShort(1) // num id
//    
//    buffer.putInt(0x409) // name
//    buffer.putInt(16 + 8 + 16 + 8 + 16 + 8) // offsetToData
//
//    buffer.putInt(0x3000 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 18) // data RVA
//    buffer.putInt(file.length().toInt) // size
//    buffer.putInt(0) // codepage
//    buffer.putInt(0) // reserved
//
//    buffer.putShort(8)
//    buffer.putChar('M')
//    buffer.putChar('A')
//    buffer.putChar('I')
//    buffer.putChar('N')
//    buffer.putChar('I')
//    buffer.putChar('C')
//    buffer.putChar('O')
//    buffer.putChar('N')
//      
//    //convert file into array of bytes
//    val fileInputStream = new FileInputStream(file);
//    fileInputStream.read(bFile);
//    fileInputStream.close();
//    
//    val bbuf = ByteBuffer.wrap(bFile)
//    bbuf.order(ByteOrder.LITTLE_ENDIAN)
//    buffer.put(bbuf.array())
//    
//    buffer.array()
//  }
  
   def compileResources(beginningOfSection: Int): Array[Byte] = { 
    
    val file = new File("testicon2.ico");
	 
    
    val bFile: Array[Byte] = Array.fill(file.length().toInt)(0);
    
    val buffer = ByteBuffer.allocate(21300)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    
    // ROOT
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(0) // num named
    buffer.putShort(2) // num id
    
    buffer.putInt(DirectoryTypeID.icon.id) // name
    buffer.putInt(0x80000000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16) // offsetToData
    
    buffer.putInt(DirectoryTypeID.groupIcon.id) // name
    buffer.putInt(0x80000000 + 16 + 8 + 8) // offsetToData
    
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(1) // num named
    buffer.putShort(0) // num id
    
    buffer.putInt(0x80000000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 16)
    buffer.putInt(0x80000000 + 16 + 8 + 8 + 16 + 8)
    
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(0) // num named
    buffer.putShort(1) // num id
    
    buffer.putInt(0x409) // name
    buffer.putInt(16 + 8 + 8 + 16 + 8 + 16 + 8) // offsetToData

    buffer.putInt(0x3000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 18 + 8 + 16 + 8 + 16 + 16) // data RVA
    buffer.putInt(20) // size
    buffer.putInt(0) // codepage
    buffer.putInt(0) // reserved
    
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(0) // num named
    buffer.putShort(1) // num id
    
    buffer.putInt(1)
    buffer.putInt(0x80000000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16)
    
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(0) // num named
    buffer.putShort(1) // num id
    
    buffer.putInt(0x409) // name
    buffer.putInt(16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16) // offsetToData

    buffer.putInt(0x3000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 18 + 8 + 16 + 8 + 16 + 16 + 22) // data RVA
    buffer.putInt(file.length().toInt - 22) // size
    buffer.putInt(0) // codepage
    buffer.putInt(0) // reserved
    
    
    buffer.putShort(8)
    buffer.putChar('M')
    buffer.putChar('A')
    buffer.putChar('I')
    buffer.putChar('N')
    buffer.putChar('I')
    buffer.putChar('C')
    buffer.putChar('O')
    buffer.putChar('N')
      
    //convert file into array of bytes
    val fileInputStream = new FileInputStream(file);
    fileInputStream.read(bFile);
    fileInputStream.close();
    
    val bbuf = ByteBuffer.wrap(bFile)
    bbuf.order(ByteOrder.LITTLE_ENDIAN)
    buffer.put(bbuf.array())
    buffer.array()
  }


  def link(asm: Assembled, addressOfData: Int, is64Bit: Boolean, hasIcon: Boolean, dlls: String*): PortableExecutable = {

    val (rawData, variables) = AsmCompiler.compileData(addressOfData, asm.data)
    
    val compiledAsm = AsmCompiler.compileAssembly(asm, variables)
    
    val unboundSymbols = compiledAsm.onePass.collect { case ImportRef(name) => name}
    
    val compiledImports = compileImports(addressOfData, rawData.size, dlls, unboundSymbols, is64Bit)

    val dosHeader = DosHeader(
	    e_magic = "MZ", 
	    e_cblp = 144,
	    e_cp = 3,
	    e_crlc = 0,
	    e_cparhdr = 4,
	    e_minalloc = 0,
	    e_maxalloc = 65535.toShort,
	    e_ss = 0,
	    e_sp = 184,
	    e_csum = 0,
	    e_ip = 0,
	    e_cs = 0,
	    e_lfarlc = 64,
	    e_ovno = 0,
	    e_res = Array.fill(4)(0.toShort),
	    e_oemid = 0,
	    e_oeminfo = 0,
	    e_res2 = Array.fill(10)(0.toShort),
	    e_lfanew = 128
    )
    
    val code = AsmCompiler.finalizeAssembly(compiledAsm, variables, compiledImports.imports, baseOffset = 0x400000 /*imagebase*/)
    
    val standardSections = List(
      SectionHeader(
        name = ".text",
        virtualSize = code.size,
        virtualAddress = 0x1000,
        sizeOfRawData = 0x200,
        pointerToRawData = 0x200,
        relocPtr = 0,
        linenumPtr = 0,
        relocations = 0,
        lineNumbers = 0,
        characteristics = Characteristic.CODE.id |
          Characteristic.EXECUTE.id |
          Characteristic.READ.id),
  
      SectionHeader(
        name = ".data",
        virtualSize = rawData.size + compiledImports.rawData.size,
        virtualAddress = 0x2000,
        sizeOfRawData = 0x200,
        pointerToRawData = 0x400,
        relocPtr = 0,
        linenumPtr = 0,
        relocations = 0,
        lineNumbers = 0,
        characteristics = Characteristic.INITIALIZED.id |
          Characteristic.READ.id |
          Characteristic.WRITE.id)
    )
    
    val resourceSection: List[SectionHeader] = List(if (hasIcon) {
        Some(SectionHeader(
        name = ".rsrc",
        virtualSize = 0x34,
        virtualAddress = 0x3000,
        sizeOfRawData = 0x2F00,
        pointerToRawData = 0x600,
        relocPtr = 0,
        linenumPtr = 0,
        relocations = 0,
        lineNumbers = 0,
        characteristics = Characteristic.INITIALIZED.id |
          Characteristic.READ.id)
      )
    } else None).flatten
    
    val sections: List[SectionHeader] = standardSections ++ resourceSection
    
    val optionalHeader = OptionalHeader(
      magic = if (is64Bit) 0x20b else 0x10b,
	  majorLinkerVersion = 2,
	  minorLinkerVersion = 50,
	  sizeOfCode = 512,
	  sizeOfInitializedData = 546,
	  sizeOfUninitData = 0,
	  addressOfEntryPoint = 0x1000,
	  baseOfCode = 0x1000,
	  baseOfData = 0x2000,
	
	  AdditionalFields(
		  imageBase = 0x400000,
		  sectionAlignment = 0x1000,
		  fileAlignment = 0x200,
		  majorOperatingSystemVersion = if (is64Bit) 5 else 4,
		  minorOperatingSystemVersion = 0,
		  majorImageVersion = 0,
		  minorImageVersion = 0,
		  majorSubsystemVersion = if (is64Bit) 5 else 4,
		  minorSubsystemVersion = 0,
		  win32Version = 0,
		  sizeOfImage = sections.last.virtualAddress + sections.last.virtualSize,
		  sizeOfHeaders = 0x200,
		  checksum = 0,
		  subsystem = 3,
		  dllCharacteristics = 0,
		  sizeOfStackReserve = 0x100000,
		  sizeOfStackCommit = 0x1000,
		  sizeOfHeapReserve = 0x100000,
		  sizeOfHeapCommit = 0x1000,
		  loaderFlags = 0,
		  numberOfRvaAndSizes = 16
	  )
    )
    
    val directories = if (hasIcon) DataDirectories(
      importSymbols = compiledImports.getImportsDirectory(addressOfData, rawData.size),
      importAddressTable = compiledImports.getIATDirectory(addressOfData, rawData.size),
      resource = ImageDataDirectory(0x3000, 11300)
    ) else
      DataDirectories(
      importSymbols = compiledImports.getImportsDirectory(addressOfData, rawData.size),
      importAddressTable = compiledImports.getIATDirectory(addressOfData, rawData.size)
    )
    
    
    
    
    
    val fileHeader = FileHeader(
		machine = if (is64Bit) 0x8664.toShort else 0x14C,
	    numberOfSections = sections.size.toShort,
	    timeDateStamp = 0x5132F2E5,
	    pointerToSymbolTable = 0, // no importance
	    numberOfSymbols = 0, // no importance
	    sizeOfOptionalHeader = if (is64Bit) 0xF0 else 0xE0,
	    characteristics = if (is64Bit) 47 else 271
	)
	
	val peHeader = new NtHeader(fileHeader, optionalHeader)
    val res: Array[Byte] = if (hasIcon) compileResources(resourceSection(0).pointerToRawData) else Array()
    PortableExecutable(dosHeader, peHeader, directories, sections, code, rawData, compiledImports, res)
  }
  
  
}