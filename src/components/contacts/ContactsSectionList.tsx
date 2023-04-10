import React, { useEffect, useState } from 'react'
import { SectionList } from 'react-native'

import Contact from './models/Contact'
import ContactSectionHeader from './ContactSectionHeader'
import ContactDivider from './ContactDivider'
import ContactCell from './ContactCell'
import contacts from './data/contacts'
import ContactHeader from './ContactHeader'

interface Section {
  title: string
  data: Contact[]
}

const ContactsSectionList = () => {
  const [data, setData] = useState<Section[]>([])
  useEffect(() => {
    const sectionedContacts: Section[] = Array.from(contacts.keys())
      .map(key => {
        return {
          title: key,
          data: contacts.get(key) ?? [],
        } as Section
      })
      .sort((aSection, bSection) => aSection.title.localeCompare(bSection.title))
    setData(sectionedContacts)
  }, [])

  return (
    <SectionList
      testID="SectionList"
      keyExtractor={({ firstName, lastName }) => firstName + lastName}
      renderItem={({ item }) => <ContactCell contact={item} />}
      renderSectionHeader={({ section: { title } }) => <ContactSectionHeader title={title} />}
      ItemSeparatorComponent={ContactDivider}
      sections={data}
      ListHeaderComponent={ContactHeader}
    />
  )
}

export default ContactsSectionList
