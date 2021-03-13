export class User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    token?: string;

    constructor(data: object) {
      Object.assign(this, data);
    }
}
