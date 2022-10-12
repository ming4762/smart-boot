const hasOwnProperty = Object.prototype.hasOwnProperty

export function isShallowEqual(paramA: any | undefined, paramB: any | undefined) {
  if ((paramA === undefined && paramB !== undefined) || (paramA !== undefined && paramB === undefined)) {
    return false
  }
  const keysA = Object.keys(paramA)
  const keysB = Object.keys(paramB)
  if (keysA.length !== keysB.length) {
    return false
  }
  for (let i = 0; i < keysA.length; i++) {
    if (!hasOwnProperty.call(paramB, keysA[i]) || paramA[keysA[i]] !== paramB[keysA[i]]) {
      return false
    }
  }
  return true
}

export function isQueriesEqual(queryA: any | undefined, queryB: any | undefined) {
  if ((queryA === undefined && queryB !== undefined) || (queryA !== undefined && queryB === undefined)) {
    return false
  }
  const keysA = Object.keys(queryA)
  const keysB = Object.keys(queryB)

  if (keysA.length !== keysB.length) {
    return false
  }

  for (let i = 0; i < keysA.length; i++) {
    if (!hasOwnProperty.call(queryB, keysA[i]) || !isShallowEqual(queryA[keysA[i]], queryB[keysA[i]])) {
      return false
    }
  }

  return true
}
