from __future__ import annotations

import sys
import zipfile
import xml.etree.ElementTree as ET


NS = {"w": "http://schemas.openxmlformats.org/wordprocessingml/2006/main"}


def main() -> int:
    if len(sys.argv) != 2:
        print("Usage: python scripts/inspect_docx.py <file.docx>")
        return 1

    path = sys.argv[1]
    with zipfile.ZipFile(path) as zf:
        data = zf.read("word/document.xml")

    root = ET.fromstring(data)
    for i, p in enumerate(root.findall(".//w:body/w:p", NS), start=1):
        style = ""
        ppr = p.find("w:pPr", NS)
        if ppr is not None:
            st = ppr.find("w:pStyle", NS)
            if st is not None:
                style = st.attrib.get(f"{{{NS['w']}}}val", "")
        texts = []
        for t in p.findall(".//w:t", NS):
            texts.append(t.text or "")
        text = "".join(texts).strip()
        if text:
            print(f"{i:04d} | {style or '-':20} | {text}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
