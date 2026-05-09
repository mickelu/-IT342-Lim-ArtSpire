from __future__ import annotations

import shutil
import sys
import tempfile
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path


W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"
NS = {"w": W_NS}
ET.register_namespace("w", W_NS)


TEMPLATE_PARTS = [
    "[Content_Types].xml",
    "_rels/.rels",
    "word/styles.xml",
    "word/numbering.xml",
    "word/settings.xml",
    "word/fontTable.xml",
    "word/_rels/fontTable.xml.rels",
    "word/theme/theme1.xml",
]


OPTIONAL_TEMPLATE_PARTS = [
    "customXML/itemProps1.xml",
    "customXML/item1.xml",
    "customXML/_rels/item1.xml.rels",
    "word/fonts/NotoSansSymbols-regular.ttf",
    "word/fonts/NotoSansSymbols-bold.ttf",
]


def read_xml(zf: zipfile.ZipFile, name: str) -> ET.Element:
    return ET.fromstring(zf.read(name))


def body_children(root: ET.Element) -> tuple[list[ET.Element], ET.Element]:
    body = root.find("w:body", NS)
    if body is None:
        raise ValueError("Document body not found")
    children = list(body)
    sect_pr = children[-1] if children and children[-1].tag == f"{{{W_NS}}}sectPr" else None
    content = children[:-1] if sect_pr is not None else children
    return content, sect_pr


def main() -> int:
    if len(sys.argv) != 4:
        print("Usage: python scripts/retheme_docx_from_template.py <template.docx> <source.docx> <output.docx>")
        return 1

    template_path = Path(sys.argv[1])
    source_path = Path(sys.argv[2])
    output_path = Path(sys.argv[3])

    with zipfile.ZipFile(template_path) as template_zip, zipfile.ZipFile(source_path) as source_zip:
        template_doc = read_xml(template_zip, "word/document.xml")
        source_doc = read_xml(source_zip, "word/document.xml")

        source_content, source_sect_pr = body_children(source_doc)
        _, template_sect_pr = body_children(template_doc)

        new_doc = ET.fromstring(ET.tostring(template_doc))
        new_body = new_doc.find("w:body", NS)
        if new_body is None:
            raise ValueError("Template body not found")

        for child in list(new_body):
            new_body.remove(child)

        for child in source_content:
            new_body.append(child)
        if source_sect_pr is not None:
            new_body.append(source_sect_pr)
        elif template_sect_pr is not None:
            new_body.append(template_sect_pr)

        output_path.parent.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory() as tmp_dir:
            tmp_path = Path(tmp_dir)

            with zipfile.ZipFile(source_path) as source_zip_extract:
                source_zip_extract.extractall(tmp_path)

            for part in TEMPLATE_PARTS:
                target_file = tmp_path / part
                target_file.parent.mkdir(parents=True, exist_ok=True)
                target_file.write_bytes(template_zip.read(part))

            for part in OPTIONAL_TEMPLATE_PARTS:
                try:
                    data = template_zip.read(part)
                except KeyError:
                    continue
                target_file = tmp_path / part
                target_file.parent.mkdir(parents=True, exist_ok=True)
                target_file.write_bytes(data)

            document_path = tmp_path / "word" / "document.xml"
            document_path.write_bytes(ET.tostring(new_doc, encoding="utf-8", xml_declaration=True))

            if not (tmp_path / "word" / "_rels" / "document.xml.rels").exists():
                raise ValueError("document.xml.rels missing after rebuild")

            with zipfile.ZipFile(output_path, "w", compression=zipfile.ZIP_DEFLATED) as out_zip:
                for file_path in sorted(tmp_path.rglob("*")):
                    if file_path.is_file():
                        arcname = file_path.relative_to(tmp_path).as_posix()
                        out_zip.write(file_path, arcname)

    print(f"Created {output_path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
