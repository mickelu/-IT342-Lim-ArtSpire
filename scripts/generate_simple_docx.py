from __future__ import annotations

import re
import sys
import zipfile
from pathlib import Path
from xml.sax.saxutils import escape


CONTENT_TYPES = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>
"""


PACKAGE_RELS = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>
"""


def paragraph_xml(text: str, size_half_points: int = 22, bold: bool = False) -> str:
    if not text:
        return "<w:p/>"
    escaped = escape(text)
    bold_xml = "<w:b/>" if bold else ""
    return (
        "<w:p>"
        "<w:r>"
        "<w:rPr>"
        f"{bold_xml}<w:sz w:val=\"{size_half_points}\"/>"
        "</w:rPr>"
        f"<w:t xml:space=\"preserve\">{escaped}</w:t>"
        "</w:r>"
        "</w:p>"
    )


def normalize_markdown(lines: list[str]) -> list[tuple[str, int, bool]]:
    normalized: list[tuple[str, int, bool]] = []
    in_code_block = False

    for raw_line in lines:
        line = raw_line.rstrip("\n")

        if line.startswith("```"):
            in_code_block = not in_code_block
            continue

        if in_code_block:
            normalized.append((line, 20, False))
            continue

        if not line.strip():
            normalized.append(("", 22, False))
            continue

        if line.startswith("### "):
            normalized.append((line[4:].strip(), 28, True))
            continue

        if line.startswith("## "):
            normalized.append((line[3:].strip(), 32, True))
            continue

        if line.startswith("# "):
            normalized.append((line[2:].strip(), 36, True))
            continue

        if line == "---":
            normalized.append(("", 22, False))
            continue

        if line.startswith("- "):
            normalized.append((f"• {line[2:].strip()}", 22, False))
            continue

        if re.match(r"^\|.+\|$", line):
            cells = [cell.strip() for cell in line.strip("|").split("|")]
            normalized.append((" | ".join(cells), 20, False))
            continue

        normalized.append((line, 22, line.startswith("**") and line.endswith("**")))

    return normalized


def build_document_xml(lines: list[str]) -> str:
    paragraphs = []
    for text, size, bold in normalize_markdown(lines):
        cleaned = text.replace("**", "")
        paragraphs.append(paragraph_xml(cleaned, size_half_points=size, bold=bold))

    body = "".join(paragraphs)
    return f"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
 xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
 xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
 xmlns:v="urn:schemas-microsoft-com:vml"
 xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
 xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
 xmlns:w10="urn:schemas-microsoft-com:office:word"
 xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
 xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
 xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
 xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
 xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
 xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
 mc:Ignorable="w14 wp14">
  <w:body>
    {body}
    <w:sectPr>
      <w:pgSz w:w="12240" w:h="15840"/>
      <w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440" w:header="708" w:footer="708" w:gutter="0"/>
    </w:sectPr>
  </w:body>
</w:document>
"""


def main() -> int:
    if len(sys.argv) != 3:
        print("Usage: python scripts/generate_simple_docx.py <input.md> <output.docx>")
        return 1

    source = Path(sys.argv[1])
    target = Path(sys.argv[2])

    lines = source.read_text(encoding="utf-8").splitlines()
    document_xml = build_document_xml(lines)

    target.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(target, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        zf.writestr("[Content_Types].xml", CONTENT_TYPES)
        zf.writestr("_rels/.rels", PACKAGE_RELS)
        zf.writestr("word/document.xml", document_xml)

    print(f"Created {target}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
