from typing import Optional, Callable
import json


Entry = tuple[str, str]
KVTransformer = Callable[[Entry], Entry]


COMMENT_KEY = '_comment'


def str_remove_start(strn: str, start: str) -> tuple[bool, str]:
    if strn.startswith(start):
        return True, strn[len(start):]
    else:
        return False, strn

def str_remove_end(strn: str, end: str) -> tuple[bool, str]:
    if strn.endswith(end):
        return True, strn[:-len(end)]
    else:
        return False, strn

def json_dump_line(entry: Optional[Entry]) -> str:
    if entry is None: return ''
    k, v = entry
    return json.dumps({ k: v }, ensure_ascii=False)[1:-1]

def load_properties_text(prop_text: str) -> list[Optional[Entry]]:
    prop_lines = [l.strip() for l in prop_text.split('\n')]
    result: list[Optional[Entry]] = []
    for line in prop_lines:
        if not line.strip():
            result.append(None)
        elif line.startswith('#'):
            result.append((COMMENT_KEY, line[1:]))
        else:
            k, v = line.split('=', maxsplit=1)
            result.append((k, v))
    return result

def format_props(props: list[Optional[Entry]], n_indent: int = 4) -> str:
    result_lines = [json_dump_line(p) for p in props]
    result_lines = [l + ',' if l else '' for l in result_lines]
    # strip heading and tailing blank lines
    while not result_lines[0]: result_lines = result_lines[1:]
    while not result_lines[-1]: result_lines = result_lines[:-1]
    # remove trailing comma
    if result_lines[-1].endswith(','): result_lines[-1] = result_lines[-1][:-1]
    return '\n'.join(['{', *(' ' * n_indent + l for l in result_lines), '}'])

def prop_apply_transformers(entry: Optional[Entry], transformers: list[KVTransformer]):
    if entry is None: return entry
    k, v = entry
    if k == COMMENT_KEY: return entry
    for t in transformers:
        entry = t(entry)
    return entry

def props_apply_transformers(props: list[Optional[Entry]], transformers: list[KVTransformer]) -> list[Optional[Entry]]:
    return [prop_apply_transformers(entry, transformers) for entry in props]

def convert_file(prop_text: str) -> str:
    props = load_properties_text(prop_text)
    props = props_apply_transformers(props, [
        old_key_converter('tile', 'block'),
        old_key_converter('item', 'item'),
    ])
    return format_props(props)

def old_key_converter(old_start: str, new_start: str) -> KVTransformer:
    def convert_block_keys(entry: Entry) -> Entry:
        k, v = entry
        is_target, k = str_remove_start(k, old_start + '.')
        if not is_target: return entry
        mod_id, k = k.split('.', maxsplit=1)
        _, k = str_remove_end(k, '.name')
        return ('.'.join((new_start, mod_id, k.replace('.', '_'))), v)
    return convert_block_keys



if __name__ == '__main__':
    from pathlib import Path
    import click

    @click.command()
    @click.argument('target-file', type=click.Path(exists=True))
    def main(target_file):
        target_file_path = Path(target_file)
        result_file_path = target_file_path.parent / (target_file_path.stem.lower() + '.json')

        prop_text = target_file_path.read_text(encoding='utf-8')
        result_text = convert_file(prop_text)
        result_file_path.write_text(result_text, encoding='utf-8')

    main()

         