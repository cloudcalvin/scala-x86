package com.scalaAsm.portableExe
package sections

import java.nio.ByteBuffer
import com.scalaAsm.portableExe.ImageDataDirectory
import java.io.File
import java.nio.ByteOrder
import java.io.FileInputStream

object ResourceGen {
  def compileResources(beginningOfSection: Int): Array[Byte] = { 
    
    val file = new File("testicon.ico");
	 
    
    val bFile: Array[Byte] = Array.fill(file.length().toInt)(0);
    
    val buffer = ByteBuffer.allocate(21300)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    
    //convert file into array of bytes
    val fileInputStream = new FileInputStream(file);
    fileInputStream.read(bFile);
    fileInputStream.close();
    
    val bbuf = ByteBuffer.wrap(bFile)
    bbuf.order(ByteOrder.LITTLE_ENDIAN)
    val icon = bbuf.array()
    icon(18) = 1 // the ordinal name has to be 1?
    
    // ROOT

    val first = NonLeafResourceDirectoryEntry(DirectoryTypeID.icon.id, 
            ResourceDirectory(List(NonLeafResourceDirectoryEntry(
                1, ResourceDirectory(List(),List(LeafResourceDirectoryEntry(
                    0x409, ImageResourceDataEntry(icon.drop(22))))))),List()))
                    
    val second = NonLeafResourceDirectoryEntry(DirectoryTypeID.groupIcon.id,
            ResourceDirectory(List(),List(NamedResourceDirectoryEntry(
                ImageResourceDirString("MAINICON"), ResourceDirectory(List(),List(LeafResourceDirectoryEntry(
                    0x409, ImageResourceDataEntry(icon.take(20)))))))))
    
    val res = RootDir(List(),List(
        first,
        second
    ))
    
    
    
//    val res = RootDir(List(),List(
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.icon.id, 
//            ResourceDirectory(List(),List())),
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.groupIcon.id,
//            ResourceDirectory(List(),List()))))
    
//    val res = RootDir(List(),List(
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.icon.id, 
//            ResourceDirectory(List(NamedResourceDirectoryEntry(ImageResourceDirString(8,"MAINICON"),
//                ResourceDirectory(List(),List()))),List())),
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.groupIcon.id,
//            ResourceDirectory(List(),List(NonLeafResourceDirectoryEntry(1,
//                ResourceDirectory(List(),List())))))
//    ))
    
//     val res = RootDir(List(),List(
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.icon.id, 
//            ResourceDirectory(List(NamedResourceDirectoryEntry(ImageResourceDirString("MAINICON"),
//                ResourceDirectory(List(),List()))),List())),
//        NonLeafResourceDirectoryEntry(DirectoryTypeID.groupIcon.id,
//            ResourceDirectory(List(),List(NonLeafResourceDirectoryEntry(1,
//                ResourceDirectory(List(),List(LeafResourceDirectoryEntry(0x409, ImageResourceDataEntry(icon))))))))
//    ))
    
    buffer.put(res.output(res))
    
//    buffer.put(ResourceDirectory(List(ImageResourceDirectoryEntry(0x80000000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 16,
//                                           0x80000000 + 16 + 8 + 8 + 16 + 8)),List()).apply)
//
//    buffer.put(ResourceDirectory(List(),List(ImageResourceDirectoryEntry(0x409, 16 + 8 + 8 + 16 + 8 + 16 + 8))).apply)                                        
//
//    buffer.put(ImageResourceDataEntry(0x3000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 18 + 8 + 16 + 8 + 16 + 16, 20).apply)
//    
//    //buffer.put(ResourceDirectory(List(),List(ImageResourceDirectoryEntry(1, 0x80000000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16))).apply)
//    
//    buffer.put(ResourceDirectory(List(),List(ImageResourceDirectoryEntry(0x409, 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16 + 8 + 16))).apply)          
//
//    buffer.put(ImageResourceDataEntry(0x3000 + 16 + 8 + 8 + 16 + 8 + 16 + 8 + 16 + 18 + 8 + 16 + 8 + 16 + 16 + 22, file.length().toInt - 22).apply)
//    
//    buffer.put(ImageResourceDirString(8,"MAINICON").apply)
      
    
    
    
    buffer.array()
  }
}

object DirectoryTypeID extends Enumeration {
    type DirType = Value
    val cursor = Value(1)
    val bitmap = Value(2)
    val icon = Value(3)
    val menu = Value(4)
    val dialogBox = Value(5)
    val stringTableEntry = Value(6)
    val fontDirectory = Value(7)
    val font = Value(8)
    val acceleratorTable = Value(9)
    val applicationDefinedResource = Value(10)
    val messageTableEntry = Value(11)
    val groupCursor = Value(12)
    val groupIcon = Value(14)
    val versionInformation = Value(16)
    val dlginclude = Value(17)
    val plugAndPlay = Value(19)
    val VXD = Value(20)
    val animatedCursor = Value(21)
    val animatedIcon = Value(22)
    val html = Value(23)
    val assemblyManifest = Value(24)
}

//object ParsedImageResourceDirectory {
//
//  def getResourceDir(input: ByteBuffer, beginningFileLocation: Int, level: Int, beginningOfSection: Int): ParsedImageResourceDirectory = {
//    val characteristics = input.getInt
//    val timeDateStamp = input.getInt
//    val majorVersion = input.getShort
//    val minorVersion = input.getShort
//    val numberOfNamedEntries = input.getShort
//    val numberOfIdEntries = input.getShort
//
//    val namedEntries = for (i <- 0 until numberOfNamedEntries) yield {
//      ParsedImageResourceDirectoryEntry.getNamedDirectoryEntry(input, beginningFileLocation, beginningOfSection, level)
//    }
//
//    val idEntries = for (i <- 0 until numberOfIdEntries) yield {
//      ParsedImageResourceDirectoryEntry.getIdDirectoryEntry(input, beginningFileLocation, beginningOfSection, level)
//    }
//
//    ParsedImageResourceDirectory(
//      characteristics, 
//      timeDateStamp, 
//      majorVersion, 
//      minorVersion, 
//      namedEntries,
//      idEntries)
//  }
//
//  def getAllNamedEntries(root: ParsedImageResourceDirectory): Seq[NamedImageResourceDirectoryEntry] = {
//      val dirs = (root.namedEntries ++ root.idEntries).collect { case x: ParsedImageResourceDirectoryEntry => x }
//      dirs.collect { case x: NamedImageResourceDirectoryEntry => x } ++ dirs.flatMap { x => getAllNamedEntries(x.directory) }
//  }
//  
//  def getResources(input: ByteBuffer, sections: Seq[SectionHeader], dir: ImageDataDirectory): ParsedImageResourceDirectory = {
//
//    val section = sections.find(section => section.virtualAddress <= dir.virtualAddress &&
//      section.virtualAddress + section.sizeOfRawData > dir.virtualAddress)
//
//    val resourceFileOffset = section.get.virtualAddress - section.get.pointerToRawData
//    input.position(dir.virtualAddress - resourceFileOffset)
//    val beginningFileLocation = input.position
//
//    val resourceRoot = getResourceDir(input, beginningFileLocation, 0, resourceFileOffset)
//
//    resourceRoot
//  }
//}

case class ImageResourceDataEntry(data: Array[Byte]) extends ResourceField {

  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(16 + data.size);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0x3000 + position + 16)
    buffer.putInt(data.size)
    buffer.putInt(0)
    buffer.putInt(0)
    buffer.put(data)
    buffer.array
  }
  
  def size = 16 + data.size
  def getChildren = Nil
}

case class ImageResourceDirString(
  name: String) extends ResourceField {
  
  val length = name.size
  
  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(name.length*2 + 2);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putShort(length.toShort) // characteristics
    
    name.toCharArray().foreach{x => buffer.putChar(x)}
    buffer.array
  }
  
  def size = name.length*2 + 2
  def getChildren = Nil
}

trait ResourceField {
  var position = 0
  def apply: Array[Byte]
  def getChildren: List[ResourceField]
  def size: Int
  def getTotalSize: Int = size + getChildren.map(_.getTotalSize).sum
}

trait DirectoryEntry extends ResourceField {
  def size = 8
}

case class NonLeafResourceDirectoryEntry (
  val name: Int,
  val childDir: ResourceDirectory) extends DirectoryEntry {
  
  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(name)
    buffer.putInt(0x80000000 + childDir.position)
    buffer.array
  }
  
  def getChildren = List(childDir)
}

case class LeafResourceDirectoryEntry (
  val name: Int,
  val data: ImageResourceDataEntry) extends DirectoryEntry {
  
  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(name)
    buffer.putInt(data.position)
    buffer.array
  }
  
  def getChildren = List(data)
}

case class NamedResourceDirectoryEntry (
  val name: ImageResourceDirString,
  val childDir: ResourceDirectory) extends DirectoryEntry {
  
  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0x80000000 + name.position)
    buffer.putInt(0x80000000 + childDir.position)
    buffer.array
  }
  
  override def size = 8
  def getChildren = List(name, childDir)
}

case class ResourceDirectory(
  namedEntries: List[DirectoryEntry],
  idEntries: List[DirectoryEntry]) extends ResourceField {
  
  def apply: Array[Byte] = {
    val buffer = ByteBuffer.allocate(16 + namedEntries.size*8 + idEntries.size*8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(namedEntries.size.toShort) // num named
    buffer.putShort(idEntries.size.toShort) // num id    
    namedEntries.foreach{entry => buffer.put(entry.apply)}  
    idEntries.foreach{entry => buffer.put(entry.apply)}
    buffer.array
  }
  
  def output(resourceField: ResourceField): Array[Byte] = {
    resourceField.apply ++ (if (resourceField.getChildren.size == 0) Array[Byte]() else resourceField.getChildren.map{child => output(child)}.reduce(_++_))
  }
  
  def size = 16 + (namedEntries ++ idEntries).map(_.size).sum
             
  def getChildren = (namedEntries ++ idEntries).flatMap(_.getChildren)
}

case class RootDir(
  namedEntries: List[DirectoryEntry],
  idEntries: List[DirectoryEntry]) extends ResourceField {
  
  def apply: Array[Byte] = {
    
    var position = 16;
    
    namedEntries.foreach{entry => entry.position = position; position += entry.size}
    idEntries.foreach{entry => entry.position = position; position += entry.size}
    
    getChildren.foreach{child => place(child, position); position += child.getTotalSize; println("z: " + child.getTotalSize)}
    
    val buffer = ByteBuffer.allocate(16 + namedEntries.size*8 + idEntries.size*8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(namedEntries.size.toShort) // num named
    buffer.putShort(idEntries.size.toShort) // num id    
    namedEntries.foreach{entry => buffer.put(entry.apply)}  
    idEntries.foreach{entry => buffer.put(entry.apply)}
    
    buffer.array
  }
  
  def place(resourceField: ResourceField, position: Int): Unit = {
    println(resourceField.getClass().getName() + ": " + position)
    var newPos = position + resourceField.size
    resourceField.position = position;
    //val childSize = resourceField.getChildren.map(_.size).sum
    resourceField.getChildren.foreach{child => place(child, newPos); newPos += child.getTotalSize}
    //println("result: " + childSize)
  }
  
  def output(resourceField: ResourceField): Array[Byte] = {
    resourceField.apply ++ (if (resourceField.getChildren.size == 0) Array[Byte]() else resourceField.getChildren.map{child => output(child)}.reduce(_++_))
  }
  
  def size = 16 + (namedEntries ++ idEntries).map(_.size).sum
  
  def getChildren = (namedEntries ++ idEntries).flatMap(_.getChildren)
}

case class ImageResourceDirectory(
  characteristics: Int,
  timeDateStamp: Int,
  majorVersion: Short,
  minorVersion: Short,
  namedEntries: Short,
  idEntries: Short){
  
  def apply(): Array[Byte] = {
    val buffer = ByteBuffer.allocate(16);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0) // characteristics
    buffer.putInt(0) // time
    buffer.putShort(0) // major version
    buffer.putShort(0) // minor version
    buffer.putShort(namedEntries) // num named
    buffer.putShort(idEntries) // num id
    buffer.array()
  }
}

//case class ParsedImageResourceDirectory(
//  characteristics: Int,
//  timeDateStamp: Int,
//  majorVersion: Short,
//  minorVersion: Short,
//  namedEntries: Seq[NamedImageResourceDirectoryEntry],
//  idEntries: Seq[ImageResourceEntry]) {
//}

case class ImageResourceDirectoryEntry(
  val name: Int,
  val offsetToData: Int) {
  
  def apply(): Array[Byte] = {
    val buffer = ByteBuffer.allocate(8);
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(name)
    buffer.putInt(offsetToData)
    buffer.array
  }
}

//trait ParsedImageResourceDirectoryEntry extends ImageResourceEntry {
//  val offsetToData: Int
//  val directory: ParsedImageResourceDirectory
//}

//case class NamedImageResourceDirectoryEntry(
//  name: String,
//  offsetToData: Int,
//  directory: ParsedImageResourceDirectory) extends ParsedImageResourceDirectoryEntry
//
//case class IdImageResourceDirectoryEntry(
//  id: Int,
//  offsetToData: Int,
//  directory: ParsedImageResourceDirectory) extends ParsedImageResourceDirectoryEntry


  
object ParsedImageResourceDirectoryEntry {
//  def getNamedDirectoryEntry(input: ByteBuffer, beginningFileLocation: Int, beginningOfSection: Int, level: Int): NamedImageResourceDirectoryEntry = {
//    val namePtr = input.getInt
//    val offsetToData = input.getInt
//    val savedPos = input.position
//    //val result = if (namePtr >>> 31 == 1) {
//    val pointer = namePtr & 0x7FFFFFFF
//    input.position(beginningFileLocation + pointer)
//    val stringLength = input.getShort
//    var name: String = ""
//    for (i <- 0 until stringLength) {
//      name += input.getShort.toChar
//    }
//    var resDir: ParsedImageResourceDirectory = null
//
//    val savedPos2 = input.position
//    input.position((offsetToData & 0x7FFFFFFF) + beginningFileLocation)
//    if (level < 2) {
//      resDir = ParsedImageResourceDirectory.getResourceDir(input, beginningFileLocation, level + 1, beginningOfSection)
//    }
//    input.position(savedPos2)
//
//    val result = NamedImageResourceDirectoryEntry(name, offsetToData, resDir)
//    //}
//    input.position(savedPos)
//    result
//  }
//
//  def getIdDirectoryEntry(input: ByteBuffer, beginningFileLocation: Int, beginningOfSection: Int, level: Int): ImageResourceEntry = {
//    val namePtr = input.getInt
//    val offsetToData = input.getInt
//    val savedPos = input.position
//    val id = namePtr & 0x7FFFFFFF
//    var result: ImageResourceEntry = null
//
//    if (id == 0x409 || id == 0x415) {
//      input.position((offsetToData & 0x7FFFFFFF) + beginningFileLocation)
//      result = ImageResourceDataEntry.getDataEntry(input, beginningFileLocation)
//    } else {
//      val savedPos = input.position
//      input.position((offsetToData & 0x7FFFFFFF) + beginningFileLocation)
//      if (level < 2) {
//        val resDir = ParsedImageResourceDirectory.getResourceDir(input, beginningFileLocation, level + 1, beginningOfSection)
//        result = IdImageResourceDirectoryEntry(id, offsetToData, resDir)
//      }
//      input.position(savedPos)
//    }
//
//    input.position(savedPos)
//    result
//  }
}

object ImageResourceDataEntry {
  def getDataEntry(input: ByteBuffer, beginningOfSection: Int): ImageResourceDataEntry = {
    val offset = input.getInt
    val size = input.getInt
    val codePage = input.getInt
    val reserved = input.getInt
    
    

    input.position(offset + beginningOfSection)
    
    val data = Array.fill(size)(0.toByte)
    input.get(data)
    ImageResourceDataEntry(data)
  }
}
