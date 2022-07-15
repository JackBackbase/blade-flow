export const downloadFile = (blob: Blob, fileName: string) => {
  const isIE11 = !!window.MSInputMethodContext && !!(document as any).documentMode;

  if (!isIE11) {
    const link = document.createElement('a');
    const url = window.URL.createObjectURL(blob);
    link.href = url;
    link.download = `${fileName}`;
    link.click();
    window.URL.revokeObjectURL(url);
  } else {
    return navigator.msSaveBlob(blob, fileName);
  }

  return true;
};
