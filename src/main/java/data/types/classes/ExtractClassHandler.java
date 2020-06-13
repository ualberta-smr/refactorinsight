package data.types.classes;

import data.RefactoringInfo;
import data.RefactoringLine;
import data.types.Handler;
import gr.uom.java.xmi.diff.ExtractClassRefactoring;
import org.refactoringminer.api.Refactoring;

public class ExtractClassHandler extends Handler {

  @Override
  public RefactoringInfo specify(Refactoring refactoring, RefactoringInfo info) {
    ExtractClassRefactoring ref = (ExtractClassRefactoring) refactoring;

    if (ref.getExtractedClass().isInterface()) {
      info.setGroup(RefactoringInfo.Group.INTERFACE);
    } else if (ref.getExtractedClass().isAbstract()) {
      info.setGroup(RefactoringInfo.Group.ABSTRACT);
    } else {
      info.setGroup(RefactoringInfo.Group.CLASS);
    }
    info.setDetailsBefore(ref.getOriginalClass().getPackageName())
        .setDetailsAfter(ref.getExtractedClass().getPackageName())
        .setElementBefore(ref.getOriginalClass().getName())
        .setElementAfter(ref.getExtractedClass().getName())
        .setNameBefore(ref.getExtractedClass().getName())
        .setNameAfter(ref.getExtractedClass().getName());

    if (ref.getAttributeOfExtractedClassTypeInOriginalClass() != null) {
      info.setThreeSided(true);
      ref.getExtractedOperations().forEach(operation -> {
        info.addMarking(
            operation.codeRange(),
            ref.getExtractedClass().codeRange(),
            ref.getAttributeOfExtractedClassTypeInOriginalClass().codeRange(),
            RefactoringLine.VisualisationType.LEFT,
            null,
            RefactoringLine.MarkingOption.NONE,
            true);
      });

      ref.getExtractedAttributes().forEach(operation -> {
        info.addMarking(
            operation.codeRange(),
            ref.getExtractedClass().codeRange(),
            ref.getAttributeOfExtractedClassTypeInOriginalClass().codeRange(),
            RefactoringLine.VisualisationType.LEFT,
            null,
            RefactoringLine.MarkingOption.NONE,
            true);
      });


      String[] nameSpace = ref.getExtractedClass().getName().split("\\.");
      String className = nameSpace[nameSpace.length - 1];

      info.addMarking(
          ref.getOriginalClass().codeRange(),
          ref.getExtractedClass().codeRange(),
          ref.getAttributeOfExtractedClassTypeInOriginalClass().codeRange(),

          RefactoringLine.VisualisationType.RIGHT,
          refactoringLine -> {
            refactoringLine.setWord(new String[] {
                null,
                className,
                null
            });
          },
          RefactoringLine.MarkingOption.EXTRACT,
          true);
    } else {
      ref.getExtractedOperations().forEach(operation -> {
        info.addMarking(
            operation.codeRange(),
            ref.getExtractedClass().codeRange(), true);
      });

      ref.getExtractedAttributes().forEach(operation -> {
        info.addMarking(
            operation.codeRange(),
            ref.getExtractedClass().codeRange(), true);
      });
    }
    return info;
  }
}
