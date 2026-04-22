export enum Role {
  GUEST,
  USER,
  MODERATOR,
  ADMINISTRATOR,
}

export function stringToRole(name: string) {
  switch (name.toLowerCase()) {
    case 'user':
      return Role.USER;
    case 'moderator':
      return Role.MODERATOR;
    case 'administrator':
      return Role.ADMINISTRATOR;
    default:
      console.error(`Invalid role ${name}`);
      return Role.GUEST;
  }
}
