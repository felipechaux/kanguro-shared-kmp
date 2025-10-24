package com.insurtech.kanguro.shared.ui.composables.claims.screens

import ScreenScaffold
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.composables.claims.model.FileAttachment
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaCaptionRegular
import com.insurtech.kanguro.shared.ui.theme.MobaSubheadBold
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.SecondaryMedium
import com.insurtech.kanguro.shared.ui.theme.spacingNanoLarge
import com.insurtech.kanguro.shared.ui.theme.spacingXxs
import com.insurtech.kanguro.shared.ui.theme.spacingXxxs
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object UploadDocumentsScreen

@Composable
fun UploadDocumentsScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: RentersClaimsViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalPlatformContext.current

    val attachments by viewModel.attachments.collectAsState()
    val hasAttachments by remember { derivedStateOf { attachments.isNotEmpty() } }

    // Document picker launcher
    val documentPickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Document,
        selectionMode = FilePickerSelectionMode.Multiple,
        onResult = { files ->
            coroutineScope.launch {
                val newAttachments = files.map { file ->
                    FileAttachment(
                        fileName = file.getName(context).toString(),
                        fileByteArray = file.readByteArray(context)
                    )
                }
                newAttachments.forEach { viewModel.addAttachment(it) }
            }
        }
    )

    // Image/Video picker launcher
    val mediaPickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.ImageVideo,
        selectionMode = FilePickerSelectionMode.Multiple,
        onResult = { files ->
            coroutineScope.launch {
                val newAttachments = files.map { file ->
                    FileAttachment(
                        fileName = file.getName(context).toString(),
                        fileByteArray = file.readByteArray(context)
                    )
                }
                newAttachments.forEach { viewModel.addAttachment(it) }
            }
        }
    )

    ScreenScaffold(
        onNext = {
            if (hasAttachments) onNext()
        },
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(MR.strings.upload_documents_title),
                style = MobaBodyRegular(),
                color = SecondaryDarkest,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacingXxxs, vertical = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 24.dp)
                    .padding(horizontal = spacingXxxs),
                thickness = 2.dp,
                color = SecondaryDarkest
            )
            Spacer(modifier = Modifier.height(spacingNanoLarge))

            // Upload buttons section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingXxs),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KanguroButton(
                    text = stringResource(MR.strings.choose_documents_button),
                    enabled = true,
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    onClick = { documentPickerLauncher.launch() }
                )

                KanguroButton(
                    text = stringResource(MR.strings.choose_images_button),
                    enabled = true,
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                    onClick = { mediaPickerLauncher.launch() }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            if (attachments.isNotEmpty()) {
                Text(
                    text = stringResource(MR.strings.attached_files_label),
                    style = MobaSubheadBold(),
                    color = SecondaryDarkest,
                    modifier = Modifier.padding(horizontal = spacingXxs)
                )
                Spacer(modifier = Modifier.height(spacingNanoLarge))
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(attachments) { attachment ->
                        val fileName = attachment.fileName.lowercase()
                        val isImage =
                            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                                ".png"
                            ) || fileName.endsWith(".HEIC") || fileName.endsWith(".HEIF")
                        val isPdf = fileName.endsWith(".pdf")
                        val isWord = fileName.endsWith(".doc") || fileName.endsWith(".docx")

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when {
                                isImage -> {
                                    AsyncImage(
                                        model = attachment.fileByteArray,
                                        contentDescription = attachment.fileName,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                                isPdf -> {
                                    Image(
                                        modifier = Modifier.size(80.dp),
                                        painter = dev.icerock.moko.resources.compose.painterResource(MR.images.ic_pdf),
                                        contentDescription = null,
                                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(SecondaryMedium)
                                    )
                                }
                                isWord -> {
                                    Image(
                                        modifier = Modifier.size(80.dp),
                                        painter = dev.icerock.moko.resources.compose.painterResource(MR.images.ic_word),
                                        contentDescription = null,
                                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(SecondaryMedium)
                                    )
                                }
                                else -> {
                                    Image(
                                        modifier = Modifier.size(80.dp),
                                        painter = dev.icerock.moko.resources.compose.painterResource(MR.images.ic_generic_doc),
                                        contentDescription = null,
                                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(SecondaryMedium)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = attachment.fileName,
                                style = MobaCaptionRegular(),
                                color = SecondaryMedium,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            KanguroButton(
                                text = stringResource(MR.strings.remove_attachment_button),
                                enabled = true,
                                fontSize = 12.sp,
                                modifier = Modifier.defaultMinSize(minWidth = 40.dp),
                                height = 35,
                                onClick = { viewModel.removeAttachment(attachment) }
                            )
                        }
                        Spacer(modifier = Modifier.width(spacingNanoLarge))
                    }
                }
                Spacer(modifier = Modifier.height(spacingNanoLarge))
            }
        }
    }
}
