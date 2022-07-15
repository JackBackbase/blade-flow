export enum Entities {
  Customer = 'customer',
  DocumentRequestList = 'documentRequestList',
  DocumentRequest = 'documentRequest',
  Comments = 'comments',
  Fileset = 'fileset',
  FileData = 'fileData',
}

export enum DocumentRequestStatus {
  Open = 'Open',
  Pending = 'Pending review',
  Cancelled = 'Cancelled',
  Approved = 'Approved',
  Rejected = 'Rejected',
}

export interface DocumentRequestCreator {
  userId: string;
  fullName: string;
}

export interface Customer {
  userId: string;
  firstName: string;
  lastName: string;
  fullName: string;
  emailAddress: string;
}

export interface DocumentRequest {
  documentType: string;
  initialNote: string;
  deadline: string;
  externalId: string;
  internalId: string;
  processInstanceId: string;
  groupId: string;
  filesetName: string;
  referenceId: string;
  status: DocumentRequestStatus;
  creator: DocumentRequestCreator;
}

export interface FileData {
  id: string;
  createdAt: string;
  createdBy: string;
  lastModifiedAt: string;
  mediaType: string;
  name: string;
  status: string;
  tempGroupId: string | null;
}

export interface Fileset {
  id: string;
  name: string;
  createdAt: string;
  lastModifiedAt: string;
  createdBy: string;
  files: FileData[];
  allowedMediaTypes: string[];
  maxFiles: number;
  maxFileSize?: number;
}

export interface Comment {
  id: string;
  content: string;
  createdAt: string;
  createdBy: string;
  creatorId: string;
  lastModifiedAt: string | null;
  parentId: string | null;
}

export enum MimeTypes {
  PNG = 'image/png',
  PDF = 'application/pdf',
  BMP = 'image/bmp',
  CSS = 'text/css',
  CSV = 'text/csv',
  DOC = 'application/msword',
  DOCX = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
  GZ = 'application/gzip',
  GIF = 'image/gif',
  HTML = 'text/html',
  JPEG = 'image/jpeg',
  JPG = 'image/jpg',
  JSON = 'application/json',
  MP3 = 'audio/mpeg',
  MPEG = 'video/mpeg',
  ODP = 'application/vnd.oasis.opendocument.presentation',
  ODS = 'application/vnd.oasis.opendocument.spreadsheet',
  ODT = 'application/vnd.oasis.opendocument.text',
  PPT = 'application/vnd.ms-powerpoint',
  PPTX = 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
  RAR = 'application/vnd.rar',
  RTF = 'application/rtf',
  TXT = 'text/plain',
  WEBA = 'audio/webm',
  WEBM = 'video/webm',
  WEBP = 'image/webp',
  XHTML = 'application/xhtml+xml',
  XLS = 'application/vnd.ms-excel',
  XLSX = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  XML = 'application/xml',
  ZIP = 'application/zip',
}

export const MIME_TYPE_MAP: { [key in string]: string } = {
  [MimeTypes.BMP]: 'BMP',
  [MimeTypes.CSS]: 'CSS',
  [MimeTypes.CSV]: 'CSV',
  [MimeTypes.DOC]: 'Microsoft Word',
  [MimeTypes.DOCX]: 'Microsoft Word (OpenXML)',
  [MimeTypes.GZ]: 'GZip Compressed Archive',
  [MimeTypes.GIF]: 'Graphics Interchange Format (GIF)',
  [MimeTypes.HTML]: 'HTML',
  [MimeTypes.JPEG]: 'JPEG',
  [MimeTypes.JPG]: 'JPG',
  [MimeTypes.JSON]: 'JSON',
  [MimeTypes.ODP]: 'OpenDocument presentation document',
  [MimeTypes.ODS]: 'OpenDocument spreadsheet document',
  [MimeTypes.ODT]: 'OpenDocument text document',
  [MimeTypes.PDF]: 'PDF',
  [MimeTypes.PPT]: 'Microsoft PowerPoint',
  [MimeTypes.PPTX]: 'Microsoft PowerPoint (OpenXML)',
  [MimeTypes.RAR]: 'RAR',
  [MimeTypes.RTF]: 'RTF',
  [MimeTypes.TXT]: 'Text',
  [MimeTypes.WEBP]: 'WEBP',
  [MimeTypes.WEBA]: 'WEBA',
  [MimeTypes.WEBM]: 'WEBM',
  [MimeTypes.XHTML]: 'XHTML',
  [MimeTypes.XLS]: 'Microsoft Excel',
  [MimeTypes.XLSX]: 'Microsoft Excel (OpenXML)',
  [MimeTypes.XML]: 'XML',
  [MimeTypes.ZIP]: 'ZIP archive',
  [MimeTypes.PNG]: 'PNG',
};
