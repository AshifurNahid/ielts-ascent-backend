-- =============================================================
-- IELTS Reading Production Seed Data
-- Covers all 13 ReadingQuestionKind types:
--   MULTIPLE_CHOICE, TRUE_FALSE_NOT_GIVEN, YES_NO_NOT_GIVEN,
--   MATCHING_HEADINGS, MATCHING_INFORMATION,
--   MATCHING_SENTENCE_ENDINGS, MATCHING_FEATURES,
--   SHORT_ANSWER, SENTENCE_COMPLETION, SUMMARY_COMPLETION,
--   DIAGRAM_COMPLETION, FLOWCHART_COMPLETION, TABLE_COMPLETION
-- 2 tests × 3 passages × ~13 questions = ~78 questions total
-- Band range: 5.5 – 8.0  |  Status: PUBLISHED
-- =============================================================


-- =============================================================
-- TEST 1: IELTS Academic Reading Practice Test A
-- =============================================================
INSERT INTO reading_test (created_at, updated_at, title, description, total_time_minutes, difficulty, premium, status)
SELECT NOW(), NOW(),
       'IELTS Academic Reading Practice Test A',
       'Three-passage academic test covering science, society, and environment with all major question types.',
       60, 'MEDIUM', FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_test WHERE title = 'IELTS Academic Reading Practice Test A');


-- =============================================================
-- TEST 2: IELTS Academic Reading Practice Test B
-- =============================================================
INSERT INTO reading_test (created_at, updated_at, title, description, total_time_minutes, difficulty, premium, status)
SELECT NOW(), NOW(),
       'IELTS Academic Reading Practice Test B',
       'Three-passage academic test covering history, technology, and psychology with advanced question types.',
       60, 'HARD', TRUE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_test WHERE title = 'IELTS Academic Reading Practice Test B');


-- =============================================================
-- PASSAGE 1 (Test A): The Coral Triangle
-- =============================================================
INSERT INTO reading_passage (created_at, updated_at, title, content, short_description, topic_tag,
                             difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status)
SELECT NOW(),
       NOW(),
       'The Coral Triangle',
       'Paragraph A
       The Coral Triangle is a roughly triangular region of the tropical marine waters of Indonesia, Malaysia, the Philippines, Papua New Guinea, Timor-Leste, and the Solomon Islands. Spanning 5.7 million square kilometres of ocean, it is recognised by scientists as the global centre of marine biodiversity. The region contains 76 per cent of all known coral species, six of the world''s seven marine turtle species, and more than 2,000 species of reef fish — nearly 37 per cent of the world''s total. For this reason, marine biologists frequently refer to the Coral Triangle as the "Amazon of the Seas."

Paragraph B
The economic significance of the Coral Triangle is immense. Approximately 120 million people live within its boundaries and depend on its resources for food and income. Fisheries in the region support livelihoods across coastal communities, while coral reef tourism generates billions of dollars annually. The reefs also provide crucial coastal protection: acting as natural barriers, they reduce wave energy by up to 97 per cent, shielding low-lying islands and shorelines from storm surges and erosion.

Paragraph C
Despite its ecological and economic importance, the Coral Triangle faces severe threats. Overfishing — including the use of destructive methods such as blast fishing and cyanide poisoning — has significantly degraded many reef systems. Coastal development, agricultural runoff, and the discharge of untreated sewage have increased sedimentation and nutrient loading in reef waters, promoting algal blooms that smother corals. Climate change compounds these pressures: rising sea surface temperatures trigger coral bleaching events, during which corals expel their symbiotic algae, turning white and becoming vulnerable to mortality.

Paragraph D
Coral bleaching is not an instant death sentence. If temperatures return to normal quickly enough, corals can recover and re-acquire their algal partners. However, the frequency and intensity of bleaching events have increased dramatically since the 1980s. The third global bleaching event, which occurred between 2014 and 2017, was the longest and most widespread on record, affecting reefs across the Pacific, Indian, and Atlantic Oceans. In the Coral Triangle specifically, some reef systems experienced consecutive bleaching events before adequate recovery was possible.

Paragraph E
Efforts to protect the Coral Triangle are coordinated through the Coral Triangle Initiative on Coral Reefs, Fisheries and Food Security (CTI-CFF), a multilateral partnership launched in 2009. The initiative brings together the six member governments to manage marine protected areas, regulate fishing, and adapt to climate change. Marine protected areas (MPAs) have shown measurable benefits: studies indicate that well-enforced MPAs can increase fish biomass by more than 400 per cent compared to unprotected areas. However, enforcement remains inconsistent across the region, and funding shortfalls have hampered progress.

Paragraph F
Scientists are also investigating strategies to increase reef resilience through assisted evolution. Researchers have selectively bred corals that survived previous bleaching events, on the hypothesis that heat tolerance may be heritable. Early trials in Australia and the Philippines suggest that thermally tolerant coral strains can survive temperatures that would kill conventional corals. Critics, however, caution that genetic interventions carry unknown ecological risks and should not substitute for addressing the root causes of climate change.',
       'An academic overview of the Coral Triangle ecosystem, its economic value, threats, and conservation strategies.',
       'environment',
       'HARD',
       6.5,
       8.0,
       18,
       FALSE,
       'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'The Coral Triangle');;


-- =============================================================
-- PASSAGE 2 (Test A): The Psychology of Decision Fatigue
-- =============================================================
INSERT INTO reading_passage (
    created_at, updated_at, title, content, short_description, topic_tag,
    difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status
)
SELECT NOW(), NOW(),
       'The Psychology of Decision Fatigue',
       'Paragraph A
       Every day, the average adult makes thousands of decisions — from trivial choices such as what to eat for breakfast to consequential ones involving finances, health, or relationships. Psychologists have long recognised that decision-making is not a limitless cognitive resource. The concept of "decision fatigue" refers to the deteriorating quality of decisions made by an individual after a long session of decision-making, and it has significant implications for how institutions and individuals should structure their daily activities.

       Paragraph B
       The phenomenon was brought into public awareness through research by social psychologist Roy Baumeister, who proposed the concept of "ego depletion" in the late 1990s. Baumeister argued that self-control and decision-making draw on a shared, finite mental resource — analogous to a muscle that tires with use. Although subsequent researchers have challenged the universality of ego depletion, particularly through large-scale replication failures in the 2010s, the practical observation that decision quality declines over time remains robust across many real-world settings.

       Paragraph C
       One of the most compelling real-world demonstrations of decision fatigue comes from a study of Israeli parole board judges. Researchers analysed more than 1,000 parole decisions made across a single day and found a striking pattern: the probability of a favourable ruling dropped from approximately 65 per cent at the start of a session to nearly zero just before a break, then rebounded sharply after the judges had eaten and rested. The researchers interpreted this as evidence that, when depleted, judges defaulted to the safer, easier decision — denial — rather than engaging in the complex reasoning required to grant parole.

       Paragraph D
       Decision fatigue manifests in consumer behaviour as well. Supermarkets and online retailers exploit the phenomenon by placing impulse-buy items near checkout points, where shoppers have already made numerous small choices and their resistance is at its lowest. Studies of car purchases show that buyers who configure vehicles with many optional add-ons in sequence — choosing engine type, then colour, then upholstery, and so on — tend to accept the default option increasingly often as the session progresses. The cumulative effect can add hundreds of dollars to the final purchase price.

       Paragraph E
       Several strategies have been proposed to mitigate decision fatigue at both individual and institutional levels. At the personal level, experts recommend scheduling high-stakes decisions for the morning, before cognitive resources are depleted. Simplifying routines — as famously practised by figures such as Steve Jobs and Barack Obama, who limited their daily clothing choices — reduces the total number of decisions made before critical ones arise. At the institutional level, decision architectures can be restructured to present default options that serve the decision-maker''s long-term interests, a practice known as "nudging."

       Paragraph F
       The concept of decision fatigue intersects with broader debates about autonomy and paternalism. Critics of nudging argue that designing environments to steer people toward particular choices — even beneficial ones — undermines individual agency. Proponents counter that since cognitive biases and fatigue will influence decisions regardless, it is preferable to engineer environments that push people toward healthier or more rational outcomes. The debate remains unresolved, but it has prompted governments and health authorities worldwide to experiment with choice architecture in contexts ranging from organ donation to retirement savings.',
       'An academic exploration of decision fatigue, its psychological basis, real-world evidence, and mitigation strategies.',
       'psychology',
       'HARD', 6.5, 8.0, 17, FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'The Psychology of Decision Fatigue');


-- =============================================================
-- PASSAGE 3 (Test A): The Industrial Revolution and Child Labour
-- =============================================================
INSERT INTO reading_passage (
    created_at, updated_at, title, content, short_description, topic_tag,
    difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status
)
SELECT NOW(), NOW(),
       'The Industrial Revolution and Child Labour Reform',
       'Paragraph A
       The Industrial Revolution, which began in Britain in the latter half of the eighteenth century, transformed the economic and social fabric of society at an unprecedented pace. As factories, mills, and mines proliferated, demand for cheap, adaptable labour grew rapidly. Children, who could be paid a fraction of adult wages and whose small bodies could navigate machinery and mine shafts inaccessible to adults, became an integral part of the industrial workforce. By the early nineteenth century, it is estimated that children under fourteen constituted between 15 and 20 per cent of the British textile workforce.

       Paragraph B
       Working conditions were dangerous and punishing. Factory children typically worked twelve to sixteen hours a day, six days a week, in poorly ventilated, dimly lit spaces. In textile mills, children known as "scavengers" were required to crawl beneath running machinery to collect loose cotton fibres — a task that claimed fingers, hands, and, in some cases, lives. In coal mines, children as young as five worked as "trappers," sitting alone in pitch darkness for up to twelve hours to open and close ventilation doors. The physical and psychological toll was immense, and accidents were commonplace.

       Paragraph C
       Public concern about child labour grew gradually throughout the early nineteenth century, fuelled by investigative journalism, parliamentary inquiries, and the advocacy of reformers such as Robert Owen and Michael Sadler. Sadler''s Select Committee, established in 1831, gathered harrowing testimony from child workers and their families. The resulting report shocked many members of Parliament and the public alike. However, opposition from mill owners and laissez-faire economists, who argued that factory legislation would undermine British industrial competitiveness, significantly delayed reform.

       Paragraph D
       Legislative progress was slow but cumulative. The Factory Act of 1833 was a landmark piece of legislation: it prohibited the employment of children under nine in textile factories, limited working hours for children aged nine to thirteen to nine hours per day, and — crucially — established a system of paid factory inspectors to enforce the law. This last provision was particularly significant because previous legislation had relied on voluntary compliance and had been largely ineffective. The Mines Act of 1842 extended protections to the mining industry, prohibiting underground work for all females and for boys under ten.

       Paragraph E
       Despite these reforms, enforcement remained patchy for decades. Employers falsified children''s ages, and inspectors were too few in number to police the thousands of factories and mines operating across the country. Many working-class families, dependent on their children''s wages for survival, actively colluded in misrepresenting ages. It was not until compulsory elementary education was introduced through the Education Act of 1870, and made free in 1891, that child labour declined substantially. By removing children from the labour market and placing them in schools, the education system accomplished what factory legislation alone had failed to achieve.

       Paragraph F
       The legacy of the child labour reform movement extends beyond Britain. The investigative and legislative techniques pioneered during this period — parliamentary inquiry, factory inspection, compulsory education — became templates adopted by reforming governments across Europe and North America throughout the nineteenth and early twentieth centuries. International child labour standards were eventually codified through the International Labour Organization, founded in 1919, and remain a cornerstone of global labour rights frameworks today.',
       'A historical account of child labour during the British Industrial Revolution and the reform movements that curtailed it.',
       'history',
       'HARD', 6.5, 8.0, 18, FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'The Industrial Revolution and Child Labour Reform');


-- =============================================================
-- PASSAGE 4 (Test B): Biomimicry in Engineering
-- =============================================================
INSERT INTO reading_passage (
    created_at, updated_at, title, content, short_description, topic_tag,
    difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status
)
SELECT NOW(), NOW(),
       'Biomimicry in Engineering',
       'Paragraph A
       Nature has been solving complex engineering problems for hundreds of millions of years. The wing of a bird, the skin of a shark, and the nest of a termite each represent solutions refined through countless generations of evolutionary pressure. Biomimicry — the practice of drawing on biological systems as inspiration for human design — has emerged as one of the most productive paradigms in contemporary engineering and materials science. Rather than starting from first principles, biomimetic engineers study organisms that have already solved analogous problems and attempt to replicate or adapt the underlying mechanisms.

       Paragraph B
       One of the earliest and most celebrated examples of biomimicry is Velcro, invented by Swiss engineer George de Mestral in 1941 after he noticed how burr seeds clung to his dog''s fur. Examination under a microscope revealed that the seeds used tiny hooks to attach to the looped fibres of the fur — a mechanism de Mestral replicated in nylon to create a reusable fastener. Velcro is now used in everything from medical devices to space suits. Though the invention predates the formal discipline of biomimicry, it established the foundational principle: observe nature, understand the mechanism, and translate it into a human-scale application.

       Paragraph C
       More recent applications have become considerably more sophisticated. The Eastgate Centre in Harare, Zimbabwe, designed by architect Mick Pearce in 1996, was modelled on the self-cooling mounds of Macrotermes michaelseni termites. These termites maintain their mounds at a remarkably constant temperature of 31°C despite external temperatures ranging from 3°C at night to 42°C during the day. They achieve this through a system of vents that open and close to regulate airflow. Pearce incorporated a passive cooling system using the same principle, eliminating the need for conventional air conditioning. The building uses 90 per cent less energy for ventilation than comparable conventional structures.

       Paragraph D
       In materials science, the study of the mantis shrimp''s dactyl club — the appendage it uses to shatter the shells of prey — has inspired a new generation of impact-resistant composite materials. The club can withstand repeated strikes equivalent to the force of a bullet without fracturing. Microscopic analysis revealed a helicoidal fibre arrangement within the club''s structure — fibres rotating in a spiral pattern that distributes stress across the material and prevents crack propagation. Engineers at the University of California, Riverside have replicated this architecture in carbon fibre composites that outperform conventional layered composites in impact resistance.

       Paragraph E
       The potential of biomimicry extends into medicine and pharmaceuticals. The adhesive properties of mussels, which attach to wet surfaces using specialised proteins called mussel adhesive proteins (MAPs), have inspired the development of surgical glues capable of bonding tissue in wet environments — a longstanding challenge for conventional adhesives. Similarly, the skin of the Namib Desert beetle, which harvests moisture from fog using a combination of hydrophilic bumps and hydrophobic troughs on its back, has influenced the design of water collection surfaces and self-cleaning coatings.

       Paragraph F
       Critics of biomimicry note that biological systems are not always the most efficient solutions by human engineering standards. Evolution operates under constraints — historical contingency, the need for organisms to reproduce before they die, and the absence of foresight — that do not apply to human design. A biological solution may be adequate for survival without being optimal. Furthermore, scaling biological mechanisms from millimetres to metres, or replicating them in synthetic materials, introduces engineering challenges that nature never faced. Proponents, however, argue that the 3.8 billion years of evolutionary trial and error embodied in living systems represent an unparalleled repository of tested solutions, and that even imperfect biological inspiration consistently accelerates the design process.',
       'An academic overview of biomimicry as an engineering discipline, with case studies from architecture, materials science, and medicine.',
       'technology',
       'HARD', 6.5, 8.0, 18, FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'Biomimicry in Engineering');


-- =============================================================
-- PASSAGE 5 (Test B): The Green Revolution
-- =============================================================
INSERT INTO reading_passage (
    created_at, updated_at, title, content, short_description, topic_tag,
    difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status
)
SELECT NOW(), NOW(),
       'The Green Revolution: Gains and Contradictions',
       'Paragraph A
       Between the 1940s and the 1970s, a series of agricultural innovations collectively known as the Green Revolution transformed food production across much of the developing world. Led by scientists including Norman Borlaug — who received the Nobel Peace Prize in 1970 — the movement introduced high-yielding varieties (HYVs) of staple crops such as wheat and rice, combined with expanded irrigation, synthetic fertilisers, and pesticide use. In countries such as India, Mexico, and the Philippines, grain yields doubled and tripled within a generation, averting the widespread famines that demographers had predicted.

       Paragraph B
       The scale of the transformation was remarkable. India, which had been heavily reliant on food aid in the 1960s, achieved self-sufficiency in wheat production by 1974 and became a net exporter of rice by the 1980s. Mexico quintupled its wheat production between 1950 and 1970. The Philippines, through the introduction of IR8 — a semi-dwarf, high-yielding rice variety developed at the International Rice Research Institute — saw rice yields increase from around 1.2 tonnes per hectare to more than 3.5 tonnes per hectare within a decade. The immediate humanitarian impact was undeniable.

       Paragraph C
       However, the Green Revolution''s legacy is contested. Critics argue that the gains were unevenly distributed. HYV crops required significant inputs — irrigation water, synthetic fertilisers, and pesticides — that only wealthier farmers could readily afford. Smallholder farmers in rain-fed areas, lacking access to credit and infrastructure, were often unable to adopt the new varieties and fell further behind economically. In some regions, the concentration of agricultural production among larger, capitalised farms accelerated rural-to-urban migration, swelling urban slum populations.

       Paragraph D
       Environmental consequences have also been significant. The intensive use of synthetic fertilisers has led to widespread nitrogen and phosphorus pollution of waterways, creating hypoxic "dead zones" in coastal waters where aquatic life cannot survive. Groundwater depletion is a serious concern in regions such as the Punjab, which has been called India''s breadbasket: the water table has fallen by several metres over the past three decades as a consequence of irrigation-intensive farming. Meanwhile, the near-exclusive cultivation of a small number of high-yielding varieties has dramatically reduced agrobiodiversity, leaving global food systems more vulnerable to crop disease.

       Paragraph E
       A further criticism concerns the neglect of certain regions and crops. Sub-Saharan Africa, whose staple crops include sorghum, millet, cassava, and yams — crops that received comparatively little attention from Green Revolution researchers — benefited far less from the movement than South and Southeast Asia. This has been attributed partly to the region''s diverse agroecological conditions, which made the development of universally applicable HYVs more difficult, and partly to a lack of political and financial attention from international donors focused on Cold War-era food security concerns in Asia.

       Paragraph F
       Contemporary agricultural scientists speak of the need for a "second Green Revolution" that addresses the shortcomings of the first. This new paradigm emphasises sustainability alongside productivity: developing drought-tolerant and disease-resistant varieties suited to smallholder conditions, reducing dependence on synthetic inputs through integrated pest management and agroecology, and harnessing genomic tools to accelerate breeding programmes. Whether advances in biotechnology — including genetically modified organisms — will play a central role remains deeply controversial, both scientifically and politically.',
       'A balanced academic account of the Green Revolution, examining its humanitarian achievements and environmental and social critiques.',
       'environment',
       'HARD', 6.5, 8.0, 18, FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'The Green Revolution: Gains and Contradictions');


-- =============================================================
-- PASSAGE 6 (Test B): The History of Timekeeping
-- =============================================================
INSERT INTO reading_passage (
    created_at, updated_at, title, content, short_description, topic_tag,
    difficulty, ielts_band_min, ielts_band_max, estimated_reading_minutes, premium, status
)
SELECT NOW(), NOW(),
       'The History of Timekeeping',
       'Paragraph A
       The measurement of time is among the oldest scientific and technological challenges faced by human civilisations. From the earliest sundials of ancient Egypt and Mesopotamia to the atomic clocks that now underpin global navigation systems, the history of timekeeping is a story of steadily increasing precision driven by the needs of agriculture, navigation, commerce, and science. Each major innovation in timekeeping technology has both reflected and enabled broader changes in social organisation and economic activity.

       Paragraph B
       The sundial, which translates the shadow cast by the sun into a measurement of time, was used in ancient Egypt as early as 1500 BCE. Its obvious limitation — dependence on sunlight — spurred the development of alternative devices. The water clock, or clepsydra, measured time by the regulated flow of water from one vessel to another and could operate at night and indoors. Clepsydrae were used across ancient Egypt, Greece, Persia, and China, and achieved remarkable accuracy by the standards of the ancient world. Julius Caesar is said to have used one to time the length of speeches in the Roman Senate.

       Paragraph C
       The mechanical clock, which used an escapement mechanism to regulate the release of stored energy, emerged in medieval Europe during the thirteenth century. Early examples were large, publicly installed tower clocks — such as the one erected at Canterbury Cathedral around 1292 — designed to regulate civic and religious life by marking the canonical hours. The invention of the mainspring in the fifteenth century enabled the miniaturisation of the clock into portable timepieces, eventually producing the pocket watch. By the seventeenth century, the pendulum clock — invented by Christiaan Huygens in 1656 — had reduced daily error from minutes to seconds.

       Paragraph D
       The demands of maritime navigation drove the next leap in precision. Determining a ship''s longitude at sea requires knowing the exact time at a reference meridian while simultaneously observing local solar noon — the difference between the two times reveals the ship''s east–west position. The British Board of Longitude, established in 1714, offered a prize of £20,000 to anyone who could solve the longitude problem. Yorkshire carpenter John Harrison devoted his career to the challenge, eventually producing his H4 marine chronometer in 1759, which lost less than five seconds over a voyage of eighty-one days — sufficient accuracy to determine longitude to within half a degree.

       Paragraph E
       The twentieth century brought two further revolutions in timekeeping. The quartz clock, commercialised in the 1920s and 1930s, exploited the piezoelectric property of quartz crystals: when subjected to an electric current, a quartz crystal vibrates at a precise and stable frequency, providing a far more accurate time base than any mechanical oscillator. By the 1970s, quartz movements had displaced mechanical movements in most consumer watches and clocks. Subsequently, atomic clocks — which use the resonance frequency of atoms (most commonly caesium-133) as their oscillator — achieved accuracies of one second in 300 million years, forming the basis of Coordinated Universal Time (UTC).

       Paragraph F
       The social implications of increasingly precise timekeeping have been profound. The standardisation of time zones in the late nineteenth century — driven largely by the needs of railway timetabling — replaced the patchwork of local solar times that had prevailed across regions. The introduction of Greenwich Mean Time as a global reference, formalised at the International Meridian Conference in 1884, was a significant act of geopolitical as well as technological standardisation. Today, the atomic clocks underpinning GPS satellites are so precise that relativistic corrections for the effects of both special and general relativity must be applied — without them, GPS position errors would accumulate at a rate of roughly ten kilometres per day.',
       'A chronological overview of the development of timekeeping technology and its social and scientific implications.',
       'history',
       'HARD', 6.0, 8.0, 17, FALSE, 'PUBLISHED'
WHERE NOT EXISTS (SELECT 1 FROM reading_passage WHERE title = 'The History of Timekeeping');


-- =============================================================
-- ASSIGN PASSAGES TO TESTS
-- =============================================================
INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 1
FROM reading_test t JOIN reading_passage p ON p.title = 'The Coral Triangle'
WHERE t.title = 'IELTS Academic Reading Practice Test A'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);

INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 2
FROM reading_test t JOIN reading_passage p ON p.title = 'The Psychology of Decision Fatigue'
WHERE t.title = 'IELTS Academic Reading Practice Test A'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);

INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 3
FROM reading_test t JOIN reading_passage p ON p.title = 'The Industrial Revolution and Child Labour Reform'
WHERE t.title = 'IELTS Academic Reading Practice Test A'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);

INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 1
FROM reading_test t JOIN reading_passage p ON p.title = 'Biomimicry in Engineering'
WHERE t.title = 'IELTS Academic Reading Practice Test B'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);

INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 2
FROM reading_test t JOIN reading_passage p ON p.title = 'The Green Revolution: Gains and Contradictions'
WHERE t.title = 'IELTS Academic Reading Practice Test B'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);

INSERT INTO reading_test_passage (created_at, updated_at, reading_test_id, passage_id, order_index)
SELECT NOW(), NOW(), t.id, p.id, 3
FROM reading_test t JOIN reading_passage p ON p.title = 'The History of Timekeeping'
WHERE t.title = 'IELTS Academic Reading Practice Test B'
  AND NOT EXISTS (SELECT 1 FROM reading_test_passage rtp WHERE rtp.reading_test_id = t.id AND rtp.passage_id = p.id);


-- =============================================================
-- QUESTIONS: The Coral Triangle (Passage 1, Test A)
-- Group 1: MATCHING_HEADINGS (Q1–Q5)
-- Group 2: TRUE_FALSE_NOT_GIVEN (Q6–Q9)
-- Group 3: SUMMARY_COMPLETION (Q10–Q13)
-- =============================================================

-- Q1 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 1, 'MATCHING_HEADINGS',
       'The reading passage has six paragraphs, A–F. Choose the correct heading for Paragraph B from the list of headings below.',
       '["i. Genetic strategies for reef survival", "ii. The economic and protective value of coral reefs", "iii. The scope and biodiversity of the Coral Triangle", "iv. Escalating threats to a fragile ecosystem", "v. International cooperation and its limitations", "vi. The mechanics and consequences of bleaching"]',
       'ii. The economic and protective value of coral reefs',
       'Paragraph B focuses entirely on the economic significance of the Coral Triangle — fisheries, tourism, and coastal protection — making heading ii the only correct match.',
       'Paragraph B', 'HARD', 1, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt = 'The reading passage has six paragraphs, A–F. Choose the correct heading for Paragraph B from the list of headings below.' AND q.passage_id = p.id);

-- Q2 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 1, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph C from the list of headings below.',
       '["i. Genetic strategies for reef survival", "ii. The economic and protective value of coral reefs", "iii. The scope and biodiversity of the Coral Triangle", "iv. Escalating threats to a fragile ecosystem", "v. International cooperation and its limitations", "vi. The mechanics and consequences of bleaching"]',
       'iv. Escalating threats to a fragile ecosystem',
       'Paragraph C enumerates the main threats — overfishing, destructive fishing, coastal development, agricultural runoff, and climate change — which collectively match "escalating threats."',
       'Paragraph C', 'HARD', 2, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt = 'Choose the correct heading for Paragraph C from the list of headings below.' AND q.passage_id = p.id);

-- Q3 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 1, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph D from the list of headings below.',
       '["i. Genetic strategies for reef survival", "ii. The economic and protective value of coral reefs", "iii. The scope and biodiversity of the Coral Triangle", "iv. Escalating threats to a fragile ecosystem", "v. International cooperation and its limitations", "vi. The mechanics and consequences of bleaching"]',
       'vi. The mechanics and consequences of bleaching',
       'Paragraph D explains how coral bleaching works, the conditions under which corals can recover, and the alarming increase in bleaching frequency and duration.',
       'Paragraph D', 'HARD', 3, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt = 'Choose the correct heading for Paragraph D from the list of headings below.' AND q.passage_id = p.id);

-- Q4 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 1, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph E from the list of headings below.',
       '["i. Genetic strategies for reef survival", "ii. The economic and protective value of coral reefs", "iii. The scope and biodiversity of the Coral Triangle", "iv. Escalating threats to a fragile ecosystem", "v. International cooperation and its limitations", "vi. The mechanics and consequences of bleaching"]',
       'v. International cooperation and its limitations',
       'Paragraph E discusses the CTI-CFF multilateral initiative, its achievements with MPAs, but also its shortcomings in enforcement and funding.',
       'Paragraph E', 'HARD', 4, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt = 'Choose the correct heading for Paragraph E from the list of headings below.' AND q.passage_id = p.id);

-- Q5 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 1, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph F from the list of headings below.',
       '["i. Genetic strategies for reef survival", "ii. The economic and protective value of coral reefs", "iii. The scope and biodiversity of the Coral Triangle", "iv. Escalating threats to a fragile ecosystem", "v. International cooperation and its limitations", "vi. The mechanics and consequences of bleaching"]',
       'i. Genetic strategies for reef survival',
       'Paragraph F focuses on assisted evolution — selectively breeding thermally tolerant corals — as a scientific strategy to enhance reef resilience.',
       'Paragraph F', 'HARD', 5, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt = 'Choose the correct heading for Paragraph F from the list of headings below.' AND q.passage_id = p.id);

-- Q6 TRUE_FALSE_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 2, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       The Coral Triangle contains more than half of all known coral species worldwide.',
       NULL, 'TRUE',
       'Paragraph A states the region contains 76 per cent of all known coral species, which is more than half. The statement is therefore TRUE.',
       'Paragraph A', 'MEDIUM', 6, NULL, 'TF_RADIO', FALSE, 'PUBLISHED', '["true-false-not-given", "detail", "statistics"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Do the following statements agree%coral species worldwide.' AND q.passage_id = p.id);

-- Q7 TRUE_FALSE_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 2, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       Coral reefs reduce incoming wave energy by more than 90 per cent.',
       NULL, 'TRUE',
       'Paragraph B states reefs reduce wave energy by up to 97 per cent, which is indeed more than 90 per cent. TRUE.',
       'Paragraph B', 'MEDIUM', 7, NULL, 'TF_RADIO', FALSE, 'PUBLISHED', '["true-false-not-given", "detail"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Do the following statements agree%90 per cent.' AND q.passage_id = p.id);

-- Q8 TRUE_FALSE_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 2, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       The 2014–2017 bleaching event was the first to affect the Indian Ocean.',
       NULL, 'FALSE',
       'Paragraph D describes the 2014–2017 event as the longest and most widespread on record, affecting all three major oceans, but does not claim it was the first to affect the Indian Ocean. In fact, the passage implies earlier events also occurred. FALSE.',
       'Paragraph D', 'HARD', 8, NULL, 'TF_RADIO', FALSE, 'PUBLISHED', '["true-false-not-given", "inference"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Do the following statements agree%Indian Ocean.' AND q.passage_id = p.id);

-- Q9 TRUE_FALSE_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 2, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       Scientists have conclusively proven that heat tolerance in corals is an inherited trait.',
       NULL, 'NOT GIVEN',
       'Paragraph F says researchers bred corals on the hypothesis that heat tolerance may be heritable, and early trials are promising. Conclusive proof is not claimed. NOT GIVEN.',
       'Paragraph F', 'HARD', 9, NULL, 'TF_RADIO', FALSE, 'PUBLISHED', '["true-false-not-given", "not-given", "inference"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Do the following statements agree%inherited trait.' AND q.passage_id = p.id);

-- Q10–Q13 SUMMARY_COMPLETION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 3, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose NO MORE THAN TWO WORDS from the passage for each answer.

       Conservation efforts in the Coral Triangle are coordinated by a (10) ____ partnership called the CTI-CFF, which was established in 2009. One of its key tools is the creation of marine (11) ____ areas, which studies show can increase fish (12) ____ by over 400 per cent in well-managed sites. However, progress has been slowed by weak (13) ____ and insufficient funding.',
       NULL, 'multilateral',
       'Paragraph E describes the CTI-CFF as "a multilateral partnership." The word "multilateral" is the correct two-word-or-fewer answer for gap 10.',
       'Paragraph E', 'HARD', 10, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["summary-completion", "paraphrase", "detail"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Complete the summary below%gap 10%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 3, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose NO MORE THAN TWO WORDS from the passage for each answer.

       Conservation efforts in the Coral Triangle are coordinated by a multilateral partnership called the CTI-CFF. One of its key tools is the creation of marine (11) ____ areas, which studies show can increase fish biomass by over 400 per cent in well-managed sites.',
       NULL, 'protected',
       'The passage refers to "marine protected areas (MPAs)." The missing word is "protected."',
       'Paragraph E', 'MEDIUM', 11, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["summary-completion", "paraphrase"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Complete the summary below%gap 11%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 3, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose NO MORE THAN TWO WORDS from the passage for each answer.

       Well-enforced marine protected areas can increase fish (12) ____ by more than 400 per cent compared to unprotected areas.',
       NULL, 'biomass',
       'Paragraph E states: "well-enforced MPAs can increase fish biomass by more than 400 per cent." The answer is "biomass."',
       'Paragraph E', 'MEDIUM', 12, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["summary-completion", "detail"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Complete the summary below%gap 12%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 3, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose NO MORE THAN TWO WORDS from the passage for each answer.

       Progress in the Coral Triangle Initiative has been slowed by weak (13) ____ and insufficient funding.',
       NULL, 'enforcement',
       'Paragraph E states "enforcement remains inconsistent" and "funding shortfalls have hampered progress." The missing word is "enforcement."',
       'Paragraph E', 'MEDIUM', 13, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["summary-completion", "paraphrase"]'
FROM reading_passage p WHERE p.title = 'The Coral Triangle'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE 'Complete the summary below%gap 13%' AND q.passage_id = p.id);


-- =============================================================
-- QUESTIONS: The Psychology of Decision Fatigue (Passage 2, Test A)
-- Group 4: YES_NO_NOT_GIVEN (Q14–Q17)
-- Group 5: MATCHING_INFORMATION (Q18–Q21)
-- Group 6: SENTENCE_COMPLETION (Q22–Q26)
-- =============================================================

-- Q14 YES_NO_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 4, 'YES_NO_NOT_GIVEN',
       'Do the following statements agree with the claims of the writer in the reading passage? Write YES, NO or NOT GIVEN.

       The writer believes that Roy Baumeister''s ego depletion theory has been fully discredited by later research.',
       NULL, 'NO',
       'The writer says large-scale replication failures challenged the universality of ego depletion, but explicitly states that "the practical observation that decision quality declines over time remains robust across many real-world settings." The writer does not accept full discrediting. NO.',
       'Paragraph B', 'HARD', 14, NULL, 'YN_RADIO', FALSE, 'PUBLISHED', '["yes-no-not-given", "writer-opinion", "inference"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%ego depletion theory has been fully discredited%' AND q.passage_id = p.id);

-- Q15 YES_NO_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 4, 'YES_NO_NOT_GIVEN',
       'Do the following statements agree with the claims of the writer in the reading passage? Write YES, NO or NOT GIVEN.

       The Israeli parole board study provides convincing evidence that decision fatigue affects professional judgement in high-stakes settings.',
       NULL, 'YES',
       'The writer presents the parole board study as "one of the most compelling real-world demonstrations of decision fatigue," clearly endorsing its significance. YES.',
       'Paragraph C', 'MEDIUM', 15, NULL, 'YN_RADIO', FALSE, 'PUBLISHED', '["yes-no-not-given", "writer-opinion"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%parole board study provides convincing evidence%' AND q.passage_id = p.id);

-- Q16 YES_NO_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 4, 'YES_NO_NOT_GIVEN',
       'Do the following statements agree with the claims of the writer in the reading passage? Write YES, NO or NOT GIVEN.

       Nudging is preferable to traditional legislation as a means of influencing public behaviour.',
       NULL, 'NOT GIVEN',
       'The writer describes both sides of the nudging debate without declaring a preference for nudging over legislation. NOT GIVEN.',
       'Paragraph F', 'HARD', 16, NULL, 'YN_RADIO', FALSE, 'PUBLISHED', '["yes-no-not-given", "not-given", "writer-opinion"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Nudging is preferable to traditional legislation%' AND q.passage_id = p.id);

-- Q17 YES_NO_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 4, 'YES_NO_NOT_GIVEN',
       'Do the following statements agree with the claims of the writer in the reading passage? Write YES, NO or NOT GIVEN.

       The debate about choice architecture and autonomy has led to practical policy experiments in some countries.',
       NULL, 'YES',
       'Paragraph F states the debate "has prompted governments and health authorities worldwide to experiment with choice architecture." YES.',
       'Paragraph F', 'MEDIUM', 17, NULL, 'YN_RADIO', FALSE, 'PUBLISHED', '["yes-no-not-given", "writer-opinion", "detail"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%practical policy experiments in some countries%' AND q.passage_id = p.id);

-- Q18–Q21 MATCHING_INFORMATION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 5, 'MATCHING_INFORMATION',
       'The reading passage has six paragraphs, A–F. Which paragraph contains the following information?

       A reference to a behavioural pattern observed among consumers making sequential product choices.',
       '["Paragraph A", "Paragraph B", "Paragraph C", "Paragraph D", "Paragraph E", "Paragraph F"]',
       'Paragraph D',
       'Paragraph D describes car buyers who accept defaults increasingly as they configure more options in sequence — a consumer behaviour pattern.',
       'Paragraph D', 'MEDIUM', 18, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-information", "location", "detail"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%sequential product choices%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 5, 'MATCHING_INFORMATION',
       'Which paragraph contains the following information?

       A suggestion that some well-known public figures deliberately minimise the number of trivial decisions they make each day.',
       '["Paragraph A", "Paragraph B", "Paragraph C", "Paragraph D", "Paragraph E", "Paragraph F"]',
       'Paragraph E',
       'Paragraph E references Steve Jobs and Barack Obama limiting clothing choices to preserve decision-making capacity for important matters.',
       'Paragraph E', 'MEDIUM', 19, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-information", "location", "example"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%trivial decisions they make each day%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 5, 'MATCHING_INFORMATION',
       'Which paragraph contains the following information?

       An analogy comparing mental resources used in decision-making to a physical entity that weakens through repeated use.',
       '["Paragraph A", "Paragraph B", "Paragraph C", "Paragraph D", "Paragraph E", "Paragraph F"]',
       'Paragraph B',
       'Paragraph B presents Baumeister''s analogy of self-control as "analogous to a muscle that tires with use."',
       'Paragraph B', 'HARD', 20, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-information", "location", "analogy"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%physical entity that weakens through repeated use%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 5, 'MATCHING_INFORMATION',
       'Which paragraph contains the following information?

       A description of how commercial environments are deliberately structured to take advantage of depleted consumer willpower.',
       '["Paragraph A", "Paragraph B", "Paragraph C", "Paragraph D", "Paragraph E", "Paragraph F"]',
       'Paragraph D',
       'Paragraph D explicitly states that supermarkets and online retailers "exploit the phenomenon by placing impulse-buy items near checkout points."',
       'Paragraph D', 'MEDIUM', 21, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["matching-information", "location", "commercial"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%depleted consumer willpower%' AND q.passage_id = p.id);

-- Q22–Q26 SENTENCE_COMPLETION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 6, 'SENTENCE_COMPLETION',
       'Complete the sentences below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Decision fatigue refers to the deteriorating quality of decisions made after a prolonged period of ____.',
       NULL, 'decision-making',
       'Paragraph A defines decision fatigue as declining quality "after a long session of decision-making." The answer is "decision-making."',
       'Paragraph A', 'EASY', 22, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["sentence-completion", "definition"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%prolonged period of ____%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 6, 'SENTENCE_COMPLETION',
       'Complete the sentences below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       In the parole board study, the probability of a favourable ruling fell to nearly zero just before a ____, after which it recovered.',
       NULL, 'break',
       'Paragraph C states the probability dropped "to nearly zero just before a break, then rebounded sharply after the judges had eaten and rested."',
       'Paragraph C', 'EASY', 23, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["sentence-completion", "detail"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%fell to nearly zero just before a ____%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 6, 'SENTENCE_COMPLETION',
       'Complete the sentences below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       The practice of designing environments to guide people towards better choices is referred to as ____.',
       NULL, 'nudging',
       'Paragraph E defines this practice as "nudging."',
       'Paragraph E', 'MEDIUM', 24, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["sentence-completion", "terminology"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%guide people towards better choices is referred to as ____%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 6, 'SENTENCE_COMPLETION',
       'Complete the sentences below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Critics of nudging argue that steering people toward certain outcomes, even beneficial ones, reduces individual ____.',
       NULL, 'agency',
       'Paragraph F states critics argue nudging "undermines individual agency."',
       'Paragraph F', 'MEDIUM', 25, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["sentence-completion", "argument", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%reduces individual ____%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 6, 'SENTENCE_COMPLETION',
       'Complete the sentences below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Experts recommend scheduling important decisions in the ____ before cognitive resources become depleted.',
       NULL, 'morning',
       'Paragraph E advises "scheduling high-stakes decisions for the morning, before cognitive resources are depleted."',
       'Paragraph E', 'EASY', 26, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["sentence-completion", "advice", "detail"]'
FROM reading_passage p WHERE p.title = 'The Psychology of Decision Fatigue'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%important decisions in the ____%' AND q.passage_id = p.id);


-- =============================================================
-- QUESTIONS: Industrial Revolution & Child Labour (Passage 3, Test A)
-- Group 7: MULTIPLE_CHOICE (Q27–Q30)
-- Group 8: SHORT_ANSWER (Q31–Q34)
-- Group 9: TABLE_COMPLETION (Q35–Q40)
-- =============================================================

-- Q27 MULTIPLE_CHOICE
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 7, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       What proportion of the British textile workforce was made up of children under fourteen in the early nineteenth century?',
       '["A. Between 5 and 10 per cent", "B. Between 10 and 15 per cent", "C. Between 15 and 20 per cent", "D. More than 25 per cent"]',
       'C. Between 15 and 20 per cent',
       'Paragraph A states: "children under fourteen constituted between 15 and 20 per cent of the British textile workforce."',
       'Paragraph A', 'EASY', 27, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["multiple-choice", "statistics", "detail"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%proportion of the British textile workforce%' AND q.passage_id = p.id);

-- Q28 MULTIPLE_CHOICE
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 7, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       What was the most significant innovation of the Factory Act of 1833 compared to previous legislation?',
       '["A. It set the minimum working age at nine", "B. It limited working hours for young children", "C. It introduced paid inspectors to enforce the law", "D. It extended protection to the mining industry"]',
       'C. It introduced paid inspectors to enforce the law',
       'Paragraph D explicitly states the inspector provision was "particularly significant because previous legislation had relied on voluntary compliance and had been largely ineffective."',
       'Paragraph D', 'HARD', 28, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["multiple-choice", "main-point", "inference"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%most significant innovation of the Factory Act of 1833%' AND q.passage_id = p.id);

-- Q29 MULTIPLE_CHOICE
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 7, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       According to the passage, why did many working-class families misrepresent their children''s ages?',
       '["A. To avoid paying school fees", "B. To comply with religious expectations", "C. Because they depended on their children''s wages", "D. Because inspectors rarely visited their areas"]',
       'C. Because they depended on their children''s wages',
       'Paragraph E states: "Many working-class families, dependent on their children''s wages for survival, actively colluded in misrepresenting ages."',
       'Paragraph E', 'MEDIUM', 29, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["multiple-choice", "reason", "detail"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%misrepresent their children''s ages%' AND q.passage_id = p.id);

-- Q30 MULTIPLE_CHOICE
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 7, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       According to the writer, what ultimately proved most effective in reducing child labour in Britain?',
       '["A. The Factory Act of 1833", "B. The Mines Act of 1842", "C. The introduction of compulsory education", "D. The campaigns of Robert Owen and Michael Sadler"]',
       'C. The introduction of compulsory education',
       'Paragraph E concludes that the education system accomplished "what factory legislation alone had failed to achieve," crediting the Education Act of 1870 and free education in 1891.',
       'Paragraph E', 'MEDIUM', 30, NULL, 'SINGLE_MCQ', FALSE, 'PUBLISHED', '["multiple-choice", "main-argument", "conclusion"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%most effective in reducing child labour%' AND q.passage_id = p.id);

-- Q31–Q34 SHORT_ANSWER
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 8, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS from the passage for each answer.

       What term was given to children who crawled beneath running machinery in textile mills to collect loose fibres?',
       NULL, 'scavengers',
       'Paragraph B identifies these children as "scavengers."',
       'Paragraph B', 'MEDIUM', 31, 3, 'FREE_TEXT', FALSE, 'PUBLISHED', '["short-answer", "vocabulary", "detail"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%crawled beneath running machinery%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 8, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS from the passage for each answer.

       What was the minimum age for underground mining work set by the Mines Act of 1842 for boys?',
       NULL, 'ten',
       'Paragraph D states the Mines Act prohibited underground work for boys under ten.',
       'Paragraph D', 'EASY', 32, 3, 'FREE_TEXT', FALSE, 'PUBLISHED', '["short-answer", "detail", "legislation"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%minimum age for underground mining%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 8, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS from the passage for each answer.

       In what year was elementary education made free in Britain?',
       NULL, '1891',
       'Paragraph E states education was made free in 1891.',
       'Paragraph E', 'EASY', 33, 3, 'FREE_TEXT', FALSE, 'PUBLISHED', '["short-answer", "date", "fact"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%elementary education made free%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 8, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS from the passage for each answer.

       What international organisation, founded in 1919, eventually codified global child labour standards?',
       NULL, 'International Labour Organization',
       'Paragraph F identifies the International Labour Organization, founded in 1919, as the body that codified international child labour standards.',
       'Paragraph F', 'MEDIUM', 34, 3, 'FREE_TEXT', FALSE, 'PUBLISHED', '["short-answer", "organisation", "fact"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%international organisation, founded in 1919%' AND q.passage_id = p.id);

-- Q35–Q40 TABLE_COMPLETION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 9, 'TABLE_COMPLETION',
       'Complete the table below. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage for each answer.

       | Legislation | Year | Key Provision |
       |---|---|---|
       | Factory Act | 1833 | Banned employment of children under (35) ____ in textile factories |
       | Factory Act | 1833 | Set up a system of paid (36) ____ to enforce the law |
       | Mines Act | (37) ____ | Banned underground work for all females and boys under 10 |
       | Education Act | (38) ____ | Introduced compulsory elementary education |',
       NULL, 'nine',
       'Paragraph D: "it prohibited the employment of children under nine in textile factories."',
       'Paragraph D', 'MEDIUM', 35, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["table-completion", "legislation", "detail"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Banned employment of children under (35)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 9, 'TABLE_COMPLETION',
       'Complete the table below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       The Factory Act of 1833 established a system of paid (36) ____ to enforce the law.',
       NULL, 'factory inspectors',
       'Paragraph D: "established a system of paid factory inspectors to enforce the law."',
       'Paragraph D', 'EASY', 36, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["table-completion", "legislation", "detail"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%system of paid (36)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 9, 'TABLE_COMPLETION',
       'Complete the table below. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage for each answer.

       The Mines Act was passed in year (37) ____.',
       NULL, '1842',
       'Paragraph D: "The Mines Act of 1842."',
       'Paragraph D', 'EASY', 37, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["table-completion", "date", "fact"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Mines Act was passed in year (37)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 9, 'TABLE_COMPLETION',
       'Complete the table below. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage for each answer.

       The Education Act that introduced compulsory elementary education was passed in year (38) ____.',
       NULL, '1870',
       'Paragraph E: "compulsory elementary education was introduced through the Education Act of 1870."',
       'Paragraph E', 'EASY', 38, 2, 'FREE_TEXT', FALSE, 'PUBLISHED', '["table-completion", "date", "fact"]'
FROM reading_passage p WHERE p.title = 'The Industrial Revolution and Child Labour Reform'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Education Act that introduced compulsory%' AND q.passage_id = p.id);


-- =============================================================
-- QUESTIONS: Biomimicry in Engineering (Passage 4, Test B)
-- Group 10: MATCHING_FEATURES (Q1–Q5)
-- Group 11: MATCHING_SENTENCE_ENDINGS (Q6–Q9)
-- Group 12: FLOWCHART_COMPLETION (Q10–Q13)
-- =============================================================

-- Q1–Q5 MATCHING_FEATURES
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 10, 'MATCHING_FEATURES',
       'Look at the following biomimetic innovations (Questions 1–5) and the list of source organisms below. Match each innovation with the correct source organism. NB You may use any letter more than once.

       List of source organisms:
       A. Burr seeds
       B. Macrotermes michaelseni termites
       C. Mantis shrimp
       D. Mussels
       E. Namib Desert beetle

       Innovation: A self-cooling passive ventilation system used in an African building.',
       '["A. Burr seeds", "B. Macrotermes michaelseni termites", "C. Mantis shrimp", "D. Mussels", "E. Namib Desert beetle"]',
       'B. Macrotermes michaelseni termites',
       'Paragraph C: the Eastgate Centre was modelled on Macrotermes michaelseni termite mound ventilation.',
       'Paragraph C', 'MEDIUM', 1, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-features", "biomimicry", "organism"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%self-cooling passive ventilation system used in an African building%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 10, 'MATCHING_FEATURES',
       'Match each innovation with the correct source organism.

       Innovation: An impact-resistant composite material with a helicoidal fibre structure.',
       '["A. Burr seeds", "B. Macrotermes michaelseni termites", "C. Mantis shrimp", "D. Mussels", "E. Namib Desert beetle"]',
       'C. Mantis shrimp',
       'Paragraph D: the mantis shrimp''s dactyl club inspired the helicoidal fibre composite material.',
       'Paragraph D', 'MEDIUM', 2, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-features", "biomimicry", "organism"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%helicoidal fibre structure%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 10, 'MATCHING_FEATURES',
       'Match each innovation with the correct source organism.

       Innovation: A reusable fastener made of nylon hooks and loops.',
       '["A. Burr seeds", "B. Macrotermes michaelseni termites", "C. Mantis shrimp", "D. Mussels", "E. Namib Desert beetle"]',
       'A. Burr seeds',
       'Paragraph B: Velcro was inspired by burr seeds clinging to fur via tiny hooks.',
       'Paragraph B', 'EASY', 3, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-features", "biomimicry", "organism"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%reusable fastener made of nylon hooks%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 10, 'MATCHING_FEATURES',
       'Match each innovation with the correct source organism.

       Innovation: A surgical glue capable of bonding tissue in wet conditions.',
       '["A. Burr seeds", "B. Macrotermes michaelseni termites", "C. Mantis shrimp", "D. Mussels", "E. Namib Desert beetle"]',
       'D. Mussels',
       'Paragraph E: mussel adhesive proteins (MAPs) inspired the development of surgical glues for wet environments.',
       'Paragraph E', 'MEDIUM', 4, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-features", "biomimicry", "organism"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%surgical glue capable of bonding tissue%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 10, 'MATCHING_FEATURES',
       'Match each innovation with the correct source organism.

       Innovation: A surface design that collects water from fog.',
       '["A. Burr seeds", "B. Macrotermes michaelseni termites", "C. Mantis shrimp", "D. Mussels", "E. Namib Desert beetle"]',
       'E. Namib Desert beetle',
       'Paragraph E: the Namib Desert beetle''s back surface inspired fog-harvesting and self-cleaning coatings.',
       'Paragraph E', 'MEDIUM', 5, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-features", "biomimicry", "organism"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%surface design that collects water from fog%' AND q.passage_id = p.id);

-- Q6–Q9 MATCHING_SENTENCE_ENDINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 11, 'MATCHING_SENTENCE_ENDINGS',
       'Complete each sentence with the correct ending, A–F, below.

       The Eastgate Centre in Harare ...',
       '["A. inspired the invention of Velcro after a chance observation.", "B. uses 90 per cent less energy for ventilation than comparable buildings.", "C. was built to withstand repeated high-impact strikes.", "D. relies on a conventional air conditioning system for cooling.", "E. is an example of biomimicry criticised for ecological risks.", "F. demonstrated that evolutionary solutions can be replicated at architectural scale."]',
       'B. uses 90 per cent less energy for ventilation than comparable buildings.',
       'Paragraph C states the Eastgate Centre "uses 90 per cent less energy for ventilation than comparable conventional structures."',
       'Paragraph C', 'MEDIUM', 6, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-sentence-endings", "detail"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%The Eastgate Centre in Harare%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 11, 'MATCHING_SENTENCE_ENDINGS',
       'Complete each sentence with the correct ending, A–F, below.

       George de Mestral''s invention of Velcro ...',
       '["A. inspired the invention of Velcro after a chance observation.", "B. uses 90 per cent less energy for ventilation than comparable buildings.", "C. was the result of observing hooks on burr seeds under a microscope.", "D. relies on a conventional air conditioning system for cooling.", "E. is an example of biomimicry criticised for ecological risks.", "F. demonstrated that evolutionary solutions can be replicated at architectural scale."]',
       'C. was the result of observing hooks on burr seeds under a microscope.',
       'Paragraph B: de Mestral examined the burr seeds under a microscope, revealing the hook mechanism he replicated in nylon.',
       'Paragraph B', 'EASY', 7, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-sentence-endings", "detail"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%George de Mestral''s invention of Velcro%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 11, 'MATCHING_SENTENCE_ENDINGS',
       'Complete each sentence with the correct ending, A–F, below.

       Critics of biomimicry argue that biological solutions ...',
       '["A. always outperform human engineering alternatives.", "B. are optimal because they result from millions of years of evolution.", "C. may be adequate for survival but not necessarily optimal by engineering standards.", "D. should replace conventional engineering methods entirely.", "E. cannot be replicated in synthetic materials at any scale.", "F. are too expensive to apply in commercial construction."]',
       'C. may be adequate for survival but not necessarily optimal by engineering standards.',
       'Paragraph F: "A biological solution may be adequate for survival without being optimal."',
       'Paragraph F', 'HARD', 8, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-sentence-endings", "argument", "inference"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Critics of biomimicry argue that biological solutions%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 11, 'MATCHING_SENTENCE_ENDINGS',
       'Complete each sentence with the correct ending, A–F, below.

       The helicoidal fibre structure found in the mantis shrimp''s dactyl club ...',
       '["A. allows the appendage to absorb heat efficiently.", "B. enables the material to prevent cracks from spreading.", "C. is too complex to be reproduced in synthetic materials.", "D. was first identified by researchers in the 1970s.", "E. has so far only been applied in medical implants.", "F. gives the club its distinctive colouration."]',
       'B. enables the material to prevent cracks from spreading.',
       'Paragraph D: the helicoidal arrangement "distributes stress across the material and prevents crack propagation."',
       'Paragraph D', 'HARD', 9, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-sentence-endings", "mechanism", "detail"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%helicoidal fibre structure found in the mantis shrimp%' AND q.passage_id = p.id);

-- Q10–Q13 FLOWCHART_COMPLETION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 12, 'FLOWCHART_COMPLETION',
       'Complete the flow chart below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       HOW THE BIOMIMICRY DESIGN PROCESS WORKS (based on Paragraph A and the Velcro example):

       Step 1: Identify a human engineering (10) ____ that requires a solution.
       ↓
       Step 2: Study organisms that have evolved to solve an (11) ____ problem.
       ↓
       Step 3: Examine the underlying (12) ____ at work in the biological system.
       ↓
       Step 4: Replicate or adapt the mechanism in a human-scale (13) ____.',
       NULL, 'problem',
       'Paragraph A: biomimetic engineers "study organisms that have already solved analogous problems." Step 1 requires the word "problem."',
       'Paragraph A', 'HARD', 10, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["flowchart-completion", "process", "paraphrase"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%HOW THE BIOMIMICRY DESIGN PROCESS WORKS%step 10%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 12, 'FLOWCHART_COMPLETION',
       'Complete the flow chart below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Step 2 of the biomimicry process: Study organisms that have evolved to solve an (11) ____ problem.',
       NULL, 'analogous',
       'Paragraph A: "organisms that have already solved analogous problems." The word is "analogous."',
       'Paragraph A', 'HARD', 11, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["flowchart-completion", "process", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%evolved to solve an (11)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 12, 'FLOWCHART_COMPLETION',
       'Complete the flow chart below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Step 3 of the biomimicry process: Examine the underlying (12) ____ at work in the biological system.',
       NULL, 'mechanism',
       'Paragraph A and Paragraph B both use the word "mechanism" to describe the biological principle being studied and replicated.',
       'Paragraph A/B', 'MEDIUM', 12, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["flowchart-completion", "process", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%underlying (12)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 12, 'FLOWCHART_COMPLETION',
       'Complete the flow chart below. Write NO MORE THAN TWO WORDS from the passage for each answer.

       Step 4 of the biomimicry process: Replicate the mechanism in a human-scale (13) ____.',
       NULL, 'application',
       'Paragraph B concludes the Velcro account with "translate it into a human-scale application." The answer is "application."',
       'Paragraph B', 'MEDIUM', 13, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["flowchart-completion", "process", "conclusion"]'
FROM reading_passage p WHERE p.title = 'Biomimicry in Engineering'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%human-scale (13)%' AND q.passage_id = p.id);


-- =============================================================
-- QUESTIONS: The Green Revolution (Passage 5, Test B)
-- Group 13: TRUE_FALSE_NOT_GIVEN (Q14–Q17)
-- Group 14: MULTIPLE_CHOICE (Q18–Q21)
-- Group 15: SUMMARY_COMPLETION (Q22–Q26)
-- =============================================================

-- Q14–Q17 TRUE_FALSE_NOT_GIVEN
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 13, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       India became self-sufficient in wheat production within a decade of introducing high-yielding varieties.',
       NULL, 'TRUE',
       'Paragraph B: India introduced HYVs in the 1960s and achieved wheat self-sufficiency by 1974 — within roughly a decade. TRUE.',
       'Paragraph B', 'MEDIUM', 14, NULL, 'TF_RADIO', TRUE, 'PUBLISHED', '["true-false-not-given", "detail", "timeline"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%self-sufficient in wheat production within a decade%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 13, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       Smallholder farmers in rain-fed regions benefited more from HYV crops than larger, wealthier farms.',
       NULL, 'FALSE',
       'Paragraph C states the opposite: wealthier farmers could afford the required inputs, while smallholders in rain-fed areas "were often unable to adopt the new varieties." FALSE.',
       'Paragraph C', 'MEDIUM', 15, NULL, 'TF_RADIO', TRUE, 'PUBLISHED', '["true-false-not-given", "contradiction", "detail"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Smallholder farmers in rain-fed regions benefited more%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 13, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       The Green Revolution''s neglect of Sub-Saharan Africa was primarily due to deliberate political decisions by African governments.',
       NULL, 'NOT GIVEN',
       'Paragraph E attributes the neglect to crop diversity challenges and lack of international donor focus — not to decisions by African governments. NOT GIVEN.',
       'Paragraph E', 'HARD', 16, NULL, 'TF_RADIO', TRUE, 'PUBLISHED', '["true-false-not-given", "not-given", "inference"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%African governments%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 13, 'TRUE_FALSE_NOT_GIVEN',
       'Do the following statements agree with the information given in the reading passage? Write TRUE, FALSE or NOT GIVEN.

       The intensive irrigation required by HYV crops has caused a fall in groundwater levels in parts of India.',
       NULL, 'TRUE',
       'Paragraph D: "The water table has fallen by several metres over the past three decades as a consequence of irrigation-intensive farming" in Punjab. TRUE.',
       'Paragraph D', 'MEDIUM', 17, NULL, 'TF_RADIO', TRUE, 'PUBLISHED', '["true-false-not-given", "environment", "detail"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%fall in groundwater levels in parts of India%' AND q.passage_id = p.id);

-- Q18–Q21 MULTIPLE_CHOICE
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 14, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       According to the passage, what was the rice yield per hectare in the Philippines before the introduction of IR8?',
       '["A. Around 0.8 tonnes", "B. Around 1.2 tonnes", "C. Around 2.5 tonnes", "D. Around 3.5 tonnes"]',
       'B. Around 1.2 tonnes',
       'Paragraph B: rice yields increased "from around 1.2 tonnes per hectare to more than 3.5 tonnes per hectare within a decade."',
       'Paragraph B', 'EASY', 18, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["multiple-choice", "statistics", "detail"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%rice yield per hectare in the Philippines%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 14, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       What environmental risk does the passage associate with reduced agrobiodiversity?',
       '["A. Increased water consumption by crops", "B. Greater vulnerability to crop disease", "C. Higher levels of soil erosion", "D. Reduced effectiveness of pesticides"]',
       'B. Greater vulnerability to crop disease',
       'Paragraph D: "the near-exclusive cultivation of a small number of high-yielding varieties has dramatically reduced agrobiodiversity, leaving global food systems more vulnerable to crop disease."',
       'Paragraph D', 'MEDIUM', 19, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["multiple-choice", "environment", "consequence"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%environmental risk does the passage associate with reduced agrobiodiversity%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 14, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       How much did Mexico increase its wheat production between 1950 and 1970?',
       '["A. It doubled", "B. It tripled", "C. It quadrupled", "D. It quintupled"]',
       'D. It quintupled',
       'Paragraph B: "Mexico quintupled its wheat production between 1950 and 1970."',
       'Paragraph B', 'EASY', 20, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["multiple-choice", "statistics", "detail"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Mexico increase its wheat production%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 14, 'MULTIPLE_CHOICE',
       'Choose the correct letter, A, B, C or D.

       What does the passage suggest is the most controversial aspect of the proposed second Green Revolution?',
       '["A. The use of integrated pest management", "B. The development of drought-tolerant varieties", "C. The role of genetically modified organisms", "D. The targeting of smallholder farmers"]',
       'C. The role of genetically modified organisms',
       'Paragraph F: "Whether advances in biotechnology — including genetically modified organisms — will play a central role remains deeply controversial, both scientifically and politically."',
       'Paragraph F', 'HARD', 21, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["multiple-choice", "controversy", "inference"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%most controversial aspect of the proposed second Green Revolution%' AND q.passage_id = p.id);

-- Q22–Q26 SUMMARY_COMPLETION
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 15, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose ONE WORD ONLY from the passage for each answer.

       The Green Revolution introduced high-(22) ____ varieties of staple crops, combined with expanded irrigation and synthetic fertilisers.',
       NULL, 'yielding',
       'Paragraph A: "high-yielding varieties (HYVs)." The missing word is "yielding."',
       'Paragraph A', 'EASY', 22, 1, 'FREE_TEXT', TRUE, 'PUBLISHED', '["summary-completion", "paraphrase", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%high-(22)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 15, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose ONE WORD ONLY from the passage for each answer.

       One environmental consequence has been nitrogen and phosphorus (23) ____ of waterways, creating coastal dead zones.',
       NULL, 'pollution',
       'Paragraph D: "The intensive use of synthetic fertilisers has led to widespread nitrogen and phosphorus pollution of waterways."',
       'Paragraph D', 'MEDIUM', 23, 1, 'FREE_TEXT', TRUE, 'PUBLISHED', '["summary-completion", "environment", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%nitrogen and phosphorus (23)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 15, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose ONE WORD ONLY from the passage for each answer.

       Sub-Saharan Africa benefited less partly because its staple crops received less (24) ____ from Green Revolution researchers.',
       NULL, 'attention',
       'Paragraph E: "crops that received comparatively little attention from Green Revolution researchers."',
       'Paragraph E', 'MEDIUM', 24, 1, 'FREE_TEXT', TRUE, 'PUBLISHED', '["summary-completion", "paraphrase", "detail"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%received less (24)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 15, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose ONE WORD ONLY from the passage for each answer.

       A proposed second Green Revolution emphasises (25) ____ alongside productivity, aiming to reduce reliance on synthetic inputs.',
       NULL, 'sustainability',
       'Paragraph F: "This new paradigm emphasises sustainability alongside productivity."',
       'Paragraph F', 'MEDIUM', 25, 1, 'FREE_TEXT', TRUE, 'PUBLISHED', '["summary-completion", "main-idea", "vocabulary"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%emphasises (25)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 15, 'SUMMARY_COMPLETION',
       'Complete the summary below. Choose ONE WORD ONLY from the passage for each answer.

       The use of (26) ____ tools to accelerate crop breeding programmes is one approach proposed in the new paradigm.',
       NULL, 'genomic',
       'Paragraph F: "harnessing genomic tools to accelerate breeding programmes."',
       'Paragraph F', 'HARD', 26, 1, 'FREE_TEXT', TRUE, 'PUBLISHED', '["summary-completion", "vocabulary", "science"]'
FROM reading_passage p WHERE p.title = 'The Green Revolution: Gains and Contradictions'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%use of (26)%' AND q.passage_id = p.id);


-- =============================================================
-- QUESTIONS: The History of Timekeeping (Passage 6, Test B)
-- Group 16: MATCHING_HEADINGS (Q27–Q31)
-- Group 17: SHORT_ANSWER (Q32–Q35)
-- Group 18: DIAGRAM_COMPLETION (Q36–Q40)
-- =============================================================

-- Q27–Q31 MATCHING_HEADINGS
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 16, 'MATCHING_HEADINGS',
       'The reading passage has six paragraphs, A–F. Choose the correct heading for Paragraph B from the list below.

       List of headings:
       i. The social and geopolitical consequences of standardised time
       ii. Early timekeeping devices and their limitations
       iii. Quartz and atomic clocks: a leap to extreme precision
       iv. The longitude problem and the prize that solved it
       v. The mechanical clock and its miniaturisation
       vi. An introduction to the history and purpose of timekeeping',
       '["i. The social and geopolitical consequences of standardised time", "ii. Early timekeeping devices and their limitations", "iii. Quartz and atomic clocks: a leap to extreme precision", "iv. The longitude problem and the prize that solved it", "v. The mechanical clock and its miniaturisation", "vi. An introduction to the history and purpose of timekeeping"]',
       'ii. Early timekeeping devices and their limitations',
       'Paragraph B covers the sundial and clepsydra — the earliest devices — and notes the sundial''s dependence on sunlight as a key limitation that drove further innovation.',
       'Paragraph B', 'MEDIUM', 27, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%heading for Paragraph B%timekeeping%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 16, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph C from the list below.

       i. The social and geopolitical consequences of standardised time
       ii. Early timekeeping devices and their limitations
       iii. Quartz and atomic clocks: a leap to extreme precision
       iv. The longitude problem and the prize that solved it
       v. The mechanical clock and its miniaturisation
       vi. An introduction to the history and purpose of timekeeping',
       '["i. The social and geopolitical consequences of standardised time", "ii. Early timekeeping devices and their limitations", "iii. Quartz and atomic clocks: a leap to extreme precision", "iv. The longitude problem and the prize that solved it", "v. The mechanical clock and its miniaturisation", "vi. An introduction to the history and purpose of timekeeping"]',
       'v. The mechanical clock and its miniaturisation',
       'Paragraph C traces the mechanical clock from large tower clocks to pocket watches via the mainspring, and the precision gains from Huygens'' pendulum clock.',
       'Paragraph C', 'MEDIUM', 28, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%heading for Paragraph C%timekeeping%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 16, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph D from the list below.

       i. The social and geopolitical consequences of standardised time
       ii. Early timekeeping devices and their limitations
       iii. Quartz and atomic clocks: a leap to extreme precision
       iv. The longitude problem and the prize that solved it
       v. The mechanical clock and its miniaturisation
       vi. An introduction to the history and purpose of timekeeping',
       '["i. The social and geopolitical consequences of standardised time", "ii. Early timekeeping devices and their limitations", "iii. Quartz and atomic clocks: a leap to extreme precision", "iv. The longitude problem and the prize that solved it", "v. The mechanical clock and its miniaturisation", "vi. An introduction to the history and purpose of timekeeping"]',
       'iv. The longitude problem and the prize that solved it',
       'Paragraph D focuses on the maritime longitude challenge, the Board of Longitude prize, and Harrison''s H4 marine chronometer.',
       'Paragraph D', 'MEDIUM', 29, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%heading for Paragraph D%timekeeping%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 16, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph E from the list below.

       i. The social and geopolitical consequences of standardised time
       ii. Early timekeeping devices and their limitations
       iii. Quartz and atomic clocks: a leap to extreme precision
       iv. The longitude problem and the prize that solved it
       v. The mechanical clock and its miniaturisation
       vi. An introduction to the history and purpose of timekeeping',
       '["i. The social and geopolitical consequences of standardised time", "ii. Early timekeeping devices and their limitations", "iii. Quartz and atomic clocks: a leap to extreme precision", "iv. The longitude problem and the prize that solved it", "v. The mechanical clock and its miniaturisation", "vi. An introduction to the history and purpose of timekeeping"]',
       'iii. Quartz and atomic clocks: a leap to extreme precision',
       'Paragraph E covers quartz clocks and atomic clocks, explaining the underlying oscillator mechanisms and their extraordinary accuracy.',
       'Paragraph E', 'MEDIUM', 30, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%heading for Paragraph E%timekeeping%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 16, 'MATCHING_HEADINGS',
       'Choose the correct heading for Paragraph F from the list below.

       i. The social and geopolitical consequences of standardised time
       ii. Early timekeeping devices and their limitations
       iii. Quartz and atomic clocks: a leap to extreme precision
       iv. The longitude problem and the prize that solved it
       v. The mechanical clock and its miniaturisation
       vi. An introduction to the history and purpose of timekeeping',
       '["i. The social and geopolitical consequences of standardised time", "ii. Early timekeeping devices and their limitations", "iii. Quartz and atomic clocks: a leap to extreme precision", "iv. The longitude problem and the prize that solved it", "v. The mechanical clock and its miniaturisation", "vi. An introduction to the history and purpose of timekeeping"]',
       'i. The social and geopolitical consequences of standardised time',
       'Paragraph F discusses time zone standardisation, Greenwich Mean Time, the International Meridian Conference, and GPS relativistic corrections — all social and geopolitical consequences.',
       'Paragraph F', 'MEDIUM', 31, NULL, 'SINGLE_MCQ', TRUE, 'PUBLISHED', '["matching-headings", "paragraph-focus"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%heading for Paragraph F%timekeeping%' AND q.passage_id = p.id);

-- Q32–Q35 SHORT_ANSWER
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 17, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS AND/OR A NUMBER from the passage for each answer.

       How much prize money did the British Board of Longitude offer to solve the longitude problem?',
       NULL, '£20,000',
       'Paragraph D: "a prize of £20,000 to anyone who could solve the longitude problem."',
       'Paragraph D', 'EASY', 32, 3, 'FREE_TEXT', TRUE, 'PUBLISHED', '["short-answer", "fact", "numbers"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%prize money did the British Board of Longitude offer%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 17, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS AND/OR A NUMBER from the passage for each answer.

       What property of quartz crystals makes them suitable as a timekeeping oscillator?',
       NULL, 'piezoelectric',
       'Paragraph E: "The quartz clock exploited the piezoelectric property of quartz crystals."',
       'Paragraph E', 'HARD', 33, 3, 'FREE_TEXT', TRUE, 'PUBLISHED', '["short-answer", "science", "terminology"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%property of quartz crystals%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 17, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS AND/OR A NUMBER from the passage for each answer.

       In what year was the International Meridian Conference held?',
       NULL, '1884',
       'Paragraph F: "formalised at the International Meridian Conference in 1884."',
       'Paragraph F', 'EASY', 34, 3, 'FREE_TEXT', TRUE, 'PUBLISHED', '["short-answer", "date", "fact"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%International Meridian Conference%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 17, 'SHORT_ANSWER',
       'Answer the questions below. Write NO MORE THAN THREE WORDS AND/OR A NUMBER from the passage for each answer.

       By approximately how much would GPS position errors accumulate per day if relativistic corrections were not applied?',
       NULL, 'ten kilometres',
       'Paragraph F: "GPS position errors would accumulate at a rate of roughly ten kilometres per day."',
       'Paragraph F', 'HARD', 35, 3, 'FREE_TEXT', TRUE, 'PUBLISHED', '["short-answer", "numbers", "science"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%GPS position errors would accumulate%' AND q.passage_id = p.id);

-- Q36–Q40 DIAGRAM_COMPLETION
-- Diagram: A simplified timeline of timekeeping milestones with blanks
INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 18, 'DIAGRAM_COMPLETION',
       'Label the timeline diagram below. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage for each answer.

       TIMELINE OF TIMEKEEPING MILESTONES

       [1500 BCE] → Sundial first used in (36) ____
            ↓
       [~250 BCE] → Clepsydra (water clock) enables timekeeping at (37) ____ and indoors
            ↓
       [~1292 CE] → First mechanical tower clock erected at (38) ____ Cathedral
            ↓
       [1656 CE]  → Christiaan Huygens invents the (39) ____ clock, reducing error to seconds per day
            ↓
       [1759 CE]  → Harrison''s H4 loses less than (40) ____ seconds over 81 days at sea',
       NULL, 'ancient Egypt',
       'Paragraph B: "The sundial was used in ancient Egypt as early as 1500 BCE." Answer: ancient Egypt.',
       'Paragraph B', 'MEDIUM', 36, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["diagram-completion", "timeline", "history"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%TIMELINE OF TIMEKEEPING MILESTONES%36%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 18, 'DIAGRAM_COMPLETION',
       'Label the timeline diagram. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage.

       The clepsydra enables timekeeping at (37) ____ and indoors.',
       NULL, 'night',
       'Paragraph B: "could operate at night and indoors." Answer: night.',
       'Paragraph B', 'EASY', 37, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["diagram-completion", "timeline", "detail"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%clepsydra enables timekeeping at (37)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 18, 'DIAGRAM_COMPLETION',
       'Label the timeline diagram. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage.

       The first mechanical tower clock was erected at (38) ____ Cathedral around 1292.',
       NULL, 'Canterbury',
       'Paragraph C: "such as the one erected at Canterbury Cathedral around 1292." Answer: Canterbury.',
       'Paragraph C', 'EASY', 38, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["diagram-completion", "timeline", "fact"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%first mechanical tower clock was erected at (38)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 18, 'DIAGRAM_COMPLETION',
       'Label the timeline diagram. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage.

       In 1656, Christiaan Huygens invented the (39) ____ clock.',
       NULL, 'pendulum',
       'Paragraph C: "the pendulum clock — invented by Christiaan Huygens in 1656." Answer: pendulum.',
       'Paragraph C', 'EASY', 39, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["diagram-completion", "timeline", "invention"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Christiaan Huygens invented the (39)%' AND q.passage_id = p.id);

INSERT INTO reading_question (created_at, updated_at, passage_id, group_number, type, prompt, options, correct_answer, explanation, answer_source_hint, difficulty, order_index, word_limit, answer_format, premium, status, tags)
SELECT NOW(), NOW(), p.id, 18, 'DIAGRAM_COMPLETION',
       'Label the timeline diagram. Write NO MORE THAN TWO WORDS AND/OR A NUMBER from the passage.

       Harrison''s H4 marine chronometer lost less than (40) ____ seconds over a voyage of eighty-one days.',
       NULL, 'five',
       'Paragraph D: "lost less than five seconds over a voyage of eighty-one days." Answer: five.',
       'Paragraph D', 'MEDIUM', 40, 2, 'FREE_TEXT', TRUE, 'PUBLISHED', '["diagram-completion", "timeline", "numbers"]'
FROM reading_passage p WHERE p.title = 'The History of Timekeeping'
                         AND NOT EXISTS (SELECT 1 FROM reading_question q WHERE q.prompt LIKE '%Harrison''s H4 marine chronometer lost less than (40)%' AND q.passage_id = p.id);
