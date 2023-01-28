import React, { useState, useMemo } from 'react';
import { createEditor } from 'slate';
import { Slate, Editable, withReact } from 'slate-react';
import { withHistory } from 'slate-history';

const Editor = () => {
  const [value, setValue] = useState(initialValue);
  const editor = useMemo(() => withHistory(withReact(createEditor())), []);
  const onContentChange = (content: any) => {
    setValue(content)
  }
  return (
      <Slate
          editor={editor}
          value={value}
          onChange={onContentChange}
      >
        <Editable placeholder="Enter some plain text..." />
      </Slate>
  );
};

const initialValue = [
  {
    children: [
      { text: 'This is editable plain text, just like a <textarea>!' },
    ],
  },
];

export default Editor;