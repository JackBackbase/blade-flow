export interface Country {
  name: string;
  isoCode: string;
  states: State[];
}

export interface State {
  name: string;
  isoCode: string;
  code: string;
}
